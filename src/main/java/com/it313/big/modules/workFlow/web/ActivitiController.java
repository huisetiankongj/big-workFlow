package com.it313.big.modules.workFlow.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
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
import com.it313.big.common.persistence.paginate.Paginate;
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
		return "modules/workFlow/deploymentList";
	}
	
	@RequiresPermissions("sys:workFlow:view")
	@RequestMapping(value = {"process/list"})
	@ResponseBody
	public Object list(@RequestBody SelfProcessDefinition pageMap){
		Paginate<SelfProcessDefinition> paginate = (Paginate<SelfProcessDefinition>)pageMap.getPaginate();
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
		Paginate<SelfProcessDefinition> selfProcessDefinitionList = new Paginate<SelfProcessDefinition>(entryList,paginate.getCurrentPage(), paginate.getRowsOfPage(),totalRow,paginate.getMenuId());
		return selfProcessDefinitionList;
	}
	
	
	/**
	 * 发布
	 * @param exportDir
	 * @param file
	 * @return
	 */
	@RequiresPermissions("sys:workFlow:deploy")
	@RequestMapping(value = {"deploy"})
	public Object deploy(@Value("#{BIG_PROPERTIES['activiti.export.diagram.path']}") String exportDir,@RequestParam(value = "file", required = false) MultipartFile file){
		String fileName = file.getOriginalFilename();
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;

            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
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
    @RequestMapping(value = "processdefinition/update/{state}/{processDefinitionId}")
    @ResponseBody
    public Object updateState(@PathVariable("state") String state, @PathVariable("processDefinitionId") String processDefinitionId) {
    	boolean b = Boolean.TRUE;
    	try {
        	if (state.equals("active")) {
                repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            } else if (state.equals("suspend")) {
                repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            }
		} catch (Exception e) {
			logger.error("error on del process", e);
			b=false;
		}
    	
        return b;
    }
	
	
}
