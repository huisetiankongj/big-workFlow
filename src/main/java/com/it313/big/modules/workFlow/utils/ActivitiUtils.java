package com.it313.big.modules.workFlow.utils;

import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

import com.it313.big.common.utils.SpringContextHolder;

public class ActivitiUtils {

	private static RepositoryService repositoryService = SpringContextHolder.getBean("repositoryService");
	private static RuntimeService runtimeService = SpringContextHolder.getBean("runtimeService");
	private static FormService formService = SpringContextHolder.getBean("formService");
	private static IdentityService identityService = SpringContextHolder.getBean("identityService");
	private static TaskService taskService = SpringContextHolder.getBean("taskService");
	private static HistoryService historyService = SpringContextHolder.getBean("historyService");
	private static ManagementService managementService = SpringContextHolder.getBean("managementService");
	
	/**
	 * 查询发布流程列表
	 */
	public static List<ProcessDefinition> getDeploymentList(){
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
												.orderByProcessDefinitionName()
												.orderByProcessDefinitionVersion()
												.desc().list();
		return list;
	}
}
