package com.it313.big.modules.workFlow.web;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
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
import com.it313.big.common.web.ActivitiAbstractController;
import com.it313.big.modules.workFlow.entity.PageMap;
import com.it313.big.modules.workFlow.utils.WorkflowUtils;

/**
 * 流程部署
 * @author Administrator
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/workFlow/")
public class ActivitiController extends ActivitiAbstractController{

	
	@RequiresPermissions("sys:workFlow:view")
	@RequestMapping(value = {"index"})
	public Object index(){
		return "modules/workFlow/deploymentList";
	}
	
	@RequiresPermissions("sys:workFlow:view")
	@RequestMapping(value = {"process/list"})
	@ResponseBody
	public Object list(@RequestBody PageMap pageMap){
		Paginate paginate = pageMap.getPaginate();
		int curPage = paginate.getCurrentPage();
		int rowOfPage = paginate.getRowsOfPage();
		int firstResult = (curPage-1)*rowOfPage;
		int maxResults = curPage*rowOfPage;
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
														.listPage(firstResult, maxResults);
		
		int totalRow = (int) processDefinitionQuery.count();
		paginate.setSize(list.size());
		paginate.setDatas(list);
		paginate.setTotalRows(totalRow);
		paginate.setTotalPages(totalRow%rowOfPage==0?totalRow/rowOfPage:totalRow/rowOfPage+1);
		return paginate;
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
