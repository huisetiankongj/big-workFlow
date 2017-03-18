package com.it313.big.modules.act.service;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it313.big.common.persistence.paginate.Paginate;
import com.it313.big.common.service.ActAbstractService;
import com.it313.big.common.utils.StringUtils;
import com.it313.big.modules.act.entity.Act;
import com.it313.big.modules.act.utils.ProcessDefCache;
import com.it313.big.modules.sys.utils.UserUtils;
import com.it313.big.modules.workFlow.entity.SelfProcessDefinition;
import com.it313.big.modules.workFlow.utils.BeanUtils;

/**
 * 流程定义相关Service
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
@Transactional(readOnly = true)
public class ActTaskService extends ActAbstractService{

	/**
	 * 获取流程列表
	 * @param category 流程分类
	 */
	public Paginate<SelfProcessDefinition> processList(Paginate<SelfProcessDefinition> page, String category) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().active().orderByProcessDefinitionKey().asc();
		
		if (StringUtils.isNotEmpty(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
		
		long totalRows = processDefinitionQuery.count();
		
		List<SelfProcessDefinition> entryList = new ArrayList<SelfProcessDefinition>();
		String[] ignore = {"deploymentName","deploymentTime"};
		
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getStartRowNum(), page.getLastRowNum());
		
		for(int i=0,len=processDefinitionList.size();i<len;i++){
			SelfProcessDefinition e = new SelfProcessDefinition();
			ProcessDefinition p = processDefinitionList.get(i);
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
		
		page.setDatas(entryList);
		
		int pageSize = page.getRowsOfPage();
		int totalPage= (int) (totalRows%pageSize==0?totalRows/pageSize:totalRows/pageSize+1);
		page.setTotalRows(totalRows);
		page.setTotalPages(totalPage);
		return page;
		
	}

	public List<Act> todoList(Act act) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		
		List<Act> result = new ArrayList<Act>();
		
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			todoTaskQuery.taskCreatedBefore(act.getEndDate());
		}
		
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
//			e.setTaskVars(task.getTaskLocalVariables());
//			System.out.println(task.getId()+"  =  "+task.getProcessVariables() + "  ========== " + task.getTaskLocalVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
//			e.setProcIns(runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
//			e.setProcExecUrl(ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
			e.setStatus("todo");
			result.add(e);
		}
		
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId)
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			toClaimQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			toClaimQuery.taskCreatedBefore(act.getEndDate());
		}
		
		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
//			e.setTaskVars(task.getTaskLocalVariables());
//			System.out.println(task.getId()+"  =  "+task.getProcessVariables() + "  ========== " + task.getTaskLocalVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
//			e.setProcIns(runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
//			e.setProcExecUrl(ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
			e.setStatus("claim");
			result.add(e);
		}
		return result;
	}
	
}
