package com.it313.big.modules.act.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.it313.big.common.persistence.paginate.Paginate;
import com.it313.big.common.service.ActAbstractService;
import com.it313.big.common.utils.StringUtils;
import com.it313.big.modules.act.entity.Act;
import com.it313.big.modules.act.entity.SelfTask;
import com.it313.big.modules.act.utils.ProcessDefCache;
import com.it313.big.modules.sys.entity.User;
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
	public Paginate<SelfProcessDefinition> processList(Act act, String category) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().active().orderByProcessDefinitionKey().asc();
		
		if (StringUtils.isNotEmpty(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
		
		long totalRows = processDefinitionQuery.count();
		
		int startRow = act.getPaginate().getStartRowNum();
		int lastRow = act.getPaginate().getLastRowNum();
		
		List<SelfProcessDefinition> entryList = new ArrayList<SelfProcessDefinition>();
		String[] ignore = {"deploymentName","deploymentTime"};
		
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(startRow, lastRow);
		
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
		
		Paginate<SelfProcessDefinition> page = new Paginate<SelfProcessDefinition>();
		
		page.setDatas(entryList);
		page.setCurrentPage(act.getPaginate().getCurrentPage());
		page.setRowsOfPage(act.getPaginate().getRowsOfPage());
		int pageSize = page.getRowsOfPage();
		int totalPage= (int) (totalRows%pageSize==0?totalRows/pageSize:totalRows/pageSize+1);
		page.setTotalRows(totalRows);
		page.setTotalPages(totalPage);
		return page;
		
	}

	public Paginate<SelfTask> todoList(Act act) {
		Paginate<Act> page = act.getPaginate();
		
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		
		List<Act> result = new ArrayList<Act>();
		
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		
		long totalRows = todoTaskQuery.count();
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		/*if (act.getBeginDate() != null){
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			todoTaskQuery.taskCreatedBefore(act.getEndDate());
		}
		*/
		String[] ignore = {"proDefName","proDefVersion","proDefId"};
		List<SelfTask> selfTaskList = new ArrayList<SelfTask>();
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			SelfTask selfTask = new SelfTask();
			BeanUtils.copyProperties(task,selfTask,ignore);
			ProcessDefinition pd = ProcessDefCache.get(task.getProcessDefinitionId());
			selfTask.setProDefId(pd.getId());
			selfTask.setProDefName(pd.getName());
			selfTask.setProDefVersion(pd.getVersion());
			selfTaskList.add(selfTask);
		}
		
		Paginate<SelfTask> pages = new Paginate<SelfTask>(selfTaskList, page.getCurrentPage(), page.getRowsOfPage(), (int) totalRows, "");
		
		/*int pageSize = page.getRowsOfPage();
		int totalPage= (int) (totalRows%pageSize==0?totalRows/pageSize:totalRows/pageSize+1);
		page.setTotalRows(totalRows);
		page.setTotalPages(totalPage);*/
		return pages;
	}

	public Paginate<Act> histoicFlowList( Act act, String startAct, String endAct){
		Paginate<Act> page = act.getPaginate();
		List<Act> actList = Lists.newArrayList();
		HistoricActivityInstanceQuery hisQuery= historyService.createHistoricActivityInstanceQuery().processInstanceId(act.getProcInsId());
		

		long totalRows = hisQuery.count();
		
		int startRow = act.getPaginate().getStartRowNum();
		int lastRow = act.getPaginate().getLastRowNum();
		
		List<HistoricActivityInstance> list =hisQuery
				.orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().listPage(startRow, lastRow);
		
		boolean start = false;
		Map<String, Integer> actMap = Maps.newHashMap();
		
		for (int i=0; i<list.size(); i++){
			
			HistoricActivityInstance histIns = list.get(i);
			
			// 过滤开始节点前的节点
			if (StringUtils.isNotBlank(startAct) && startAct.equals(histIns.getActivityId())){
				start = true;
			}
			if (StringUtils.isNotBlank(startAct) && !start){
				continue;
			}
			
			// 只显示开始节点和结束节点，并且执行人不为空的任务
			if (StringUtils.isNotBlank(histIns.getAssignee())
					 || "startEvent".equals(histIns.getActivityType())
					 || "endEvent".equals(histIns.getActivityType())){
				
				// 给节点增加一个序号
				Integer actNum = actMap.get(histIns.getActivityId());
				if (actNum == null){
					actMap.put(histIns.getActivityId(), actMap.size());
				}
				
				Act e = new Act();
				e.setHistIns(histIns);
				// 获取流程发起人名称
				if ("startEvent".equals(histIns.getActivityType())){
					List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery().processInstanceId(act.getProcInsId()).orderByProcessInstanceStartTime().asc().list();
//					List<HistoricIdentityLink> il = historyService.getHistoricIdentityLinksForProcessInstance(procInsId);
					if (il.size() > 0){
						if (StringUtils.isNotBlank(il.get(0).getStartUserId())){
							User user = UserUtils.getByLoginName(il.get(0).getStartUserId());
							if (user != null){
								e.setAssignee(histIns.getAssignee());
								e.setAssigneeName(user.getName());
							}
						}
					}
				}
				// 获取任务执行人名称
				if (StringUtils.isNotEmpty(histIns.getAssignee())){
					User user = UserUtils.getByLoginName(histIns.getAssignee());
					if (user != null){
						e.setAssignee(histIns.getAssignee());
						e.setAssigneeName(user.getName());
					}
				}
				// 获取意见评论内容
				if (StringUtils.isNotBlank(histIns.getTaskId())){
					List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
					if (commentList.size()>0){
						e.setComment(commentList.get(0).getFullMessage());
					}
				}
				actList.add(e);
			}
			
			// 过滤结束节点后的节点
			if (StringUtils.isNotBlank(endAct) && endAct.equals(histIns.getActivityId())){
				boolean bl = false;
				Integer actNum = actMap.get(histIns.getActivityId());
				// 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
				for (int j=i+1; j<list.size(); j++){
					HistoricActivityInstance hi = list.get(j);
					Integer actNumA = actMap.get(hi.getActivityId());
					if ((actNumA != null && actNumA < actNum) || StringUtils.equals(hi.getActivityId(), histIns.getActivityId())){
						bl = true;
					}
				}
				if (!bl){
					break;
				}
			}
		}
		page.setDatas(actList);
		page.setTotalRows(totalRows);
		return page;
	}

	/**
	 * 提交任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param vars 任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, Map<String, Object> vars){
		complete(taskId, procInsId, comment, "", vars);
	}
	
	/**
	 * 提交任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param title			流程标题，显示在待办任务标题
	 * @param vars 任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars){
		// 添加意见
		if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)){
			taskService.addComment(taskId, procInsId, comment);
		}
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// 设置流程标题
		if (StringUtils.isNotBlank(title)){
			vars.put("title", title);
		}
		
		// 提交任务
		taskService.complete(taskId, vars);
	}

	/**
	 * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
	 * @return
	 */
	public String getFormKey(String procDefId, String taskDefKey){
		String formKey = "";
		if (StringUtils.isNotBlank(procDefId)){
			if (StringUtils.isNotBlank(taskDefKey)){
				try{
					formKey = formService.getTaskFormKey(procDefId, taskDefKey);
				}catch (Exception e) {
					formKey = "";
				}
			}
			if (StringUtils.isBlank(formKey)){
				formKey = formService.getStartFormKey(procDefId);
			}
			if (StringUtils.isBlank(formKey)){
				formKey = "/404";
			}
		}
		logger.debug("getFormKey: {}", formKey);
		return formKey;
	}
	
	/**
	 * 获取流程实例对象
	 * @param procInsId
	 * @return
	 */
	@Transactional(readOnly = false)
	public ProcessInstance getProcIns(String procInsId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
	}

}
