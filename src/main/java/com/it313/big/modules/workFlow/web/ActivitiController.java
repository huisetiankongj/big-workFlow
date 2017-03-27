package com.it313.big.modules.workFlow.web;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.it313.big.common.persistence.paginate.ActPaginate;
import com.it313.big.common.web.ActAbstractController;
import com.it313.big.modules.workFlow.entity.SelfProcessDefinition;
import com.it313.big.modules.workFlow.utils.BeanUtils;
import com.it313.big.modules.workFlow.utils.WorkflowUtils;

/**
 * 流程部署
 * @author Administrator
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/workFlow/")
public class ActivitiController extends ActAbstractController{

	
	@RequiresPermissions("sys:workFlow:view")
	@RequestMapping(value = {"index"})
	public Object index(){
		return "modules/workFlow/processDefinitionList";
	}
	
	@RequiresPermissions("sys:workFlow:view")
	@RequestMapping(value = {"process/list"})
	@ResponseBody
	public Object list(@RequestBody SelfProcessDefinition pageMap){
		ActPaginate<SelfProcessDefinition> paginate = (ActPaginate<SelfProcessDefinition>)pageMap.getPaginate();
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
														.orderByProcessDefinitionVersion().asc()
														.listPage(paginate.getStartRowNum(), paginate.getLastRowNum());
		int totalRow = (int) processDefinitionQuery.count();
		
		List<SelfProcessDefinition> entryList = new ArrayList<SelfProcessDefinition>();
		String[] ignore = {"deploymentName","deploymentTime"};
		
		for(int i=0,len=list.size();i<len;i++){
			SelfProcessDefinition e = new SelfProcessDefinition();
			ProcessDefinition p = list.get(i);
			try {
				BeanUtils.copyProperties(p,e,ignore);
				String deploymentId = p.getDeploymentId();
		        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
		        e.setDeploymentName(deployment.getName());
		        e.setDeploymentTime(deployment.getDeploymentTime());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			entryList.add(e);
		}
		ActPaginate<SelfProcessDefinition> selfProcessDefinitionList = new ActPaginate<SelfProcessDefinition>(entryList,paginate.getCurrentPage(), paginate.getRowsOfPage(),totalRow,paginate.getMenuId());
		return selfProcessDefinitionList;
	}
	
	@RequiresPermissions("sys:workFlow:view")
	@RequestMapping(value = {"process/form"})
	public Object form(){
		return "modules/workFlow/processDefinitionForm";
	}
	
	 /**
     * 读取资源，通过流程ID
     *
     * @param resourceType      资源类型(xml|image)
     * @param processInstanceId 流程实例ID
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/resource/processInstance")
    public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream resourceAsStream = null;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();

        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
	
	/**
	 * 发布
	 * @param exportDir
	 * @param file
	 * @return
	 */
	@RequiresPermissions("sys:workFlow:deploy")
	@RequestMapping(value = {"deploy"})
	public Object deploy(@Value("#{BIG_PROPERTIES['activiti.export.diagram.path']}") String exportDir,@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request){
		String fileName = file.getOriginalFilename();
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;
            
            String deploymentName = "";
            if(request.getParameter("name")!=null){
            	deploymentName = request.getParameter("name");
            }
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).name(deploymentName).deploy();
            } else {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).name(deploymentName).deploy();
            }

            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            for (ProcessDefinition processDefinition : list) {
                WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
            }

        } catch (Exception e) {
            logger.error("error on deploy process, because of file input stream", e);
        }

        return "modules/workFlow/deploymentList";
	}
	
	/**
	 * 删除部署流程，级联删除流程实例
	 * @param deploymentId
	 * @return
	 */
	@RequiresPermissions("sys:workFlow:update")
	@RequestMapping(value = {"process/delete"})
	@ResponseBody
	public Object delDeployment(@RequestParam("deploymentId") String deploymentId){
		boolean b = Boolean.TRUE;
		try {
			repositoryService.deleteDeployment(deploymentId, true);
		} catch (Exception e) {
			logger.error("error on del process", e);
			b=false;
		}
		return b;
	}
	
	/**
     * 挂起、激活流程实例
     */
	@RequiresPermissions("sys:workFlow:update")
    @RequestMapping(value = "process/updateState")
    @ResponseBody
    public Object updateState(@RequestParam("state") String state, @RequestParam("processDefinitionId") String processDefinitionId) {
    	boolean b = Boolean.TRUE;
    	try {
        	if (state.equals("1")) {
                repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            } else if (state.equals("0")) {
                repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            }
		} catch (Exception e) {
			logger.error("error on del process", e);
			b=false;
		}
    	
        return b;
    }
	
	/**
	 * 转换model
	 * @param processDefinitionId
	 * @return
	 */
	@RequiresPermissions("sys:workFlow:update")
	@RequestMapping(value = {"process/convertToModel"})
	@ResponseBody
	public Object convertToModel(@RequestParam("processDefinitionId") String processDefinitionId){
		boolean b = Boolean.TRUE;
    	try {
    		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId).singleResult();
            InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                    processDefinition.getResourceName());
            XMLInputFactory xif = XMLInputFactory.newInstance();
            InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

            BpmnJsonConverter converter = new BpmnJsonConverter();
            com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
            Model modelData = repositoryService.newModel();
            modelData.setKey(processDefinition.getKey());
            modelData.setName(processDefinition.getResourceName());
            modelData.setCategory(processDefinition.getDeploymentId());

            ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
            modelData.setMetaInfo(modelObjectNode.toString());
            //保存model
            repositoryService.saveModel(modelData);

            repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
		} catch (Exception e) {
			logger.error("error on convertToModel", e);
			b = false;
		}
    	
        return b;
	 
	}
	
	
}
