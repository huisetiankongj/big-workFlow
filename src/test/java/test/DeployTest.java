package test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class DeployTest {

	private static RepositoryService repositoryService;
	private static RuntimeService runtimeService;
	private static TaskService taskService;
	
	//初始化流程引擎
	private static void initProcessEngine(){
		ProcessEngineConfiguration config = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("course/shapter01/activiti.cfg.xml");
		System.out.println(config.getDatabaseSchemaUpdate());
		//创建流程引擎
		ProcessEngine processEngine = config.buildProcessEngine();
		//获取各个service
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
	}
	
	//发布流程
	private static void deployFlow() throws FileNotFoundException{
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream("D:/workspace/big-workFlow/src/main/resources/act/deployments/leave.zip"));
		repositoryService.createDeployment()
						 .name("请假流程")
						 .addZipInputStream(zipInputStream)
						 .deploy();
						 
	}
	
	//获取发布信息
	private static void getDeployment(String key){
		List<Deployment> deploymentList = repositoryService.createDeploymentQuery().processDefinitionKey("leave").list();
		for(Deployment d :deploymentList){
			System.out.println(d.getId()+"====="+d.getName());
		}
	}
	//获取流程实例
	private static void getInstanceDefine(String key){
		List<ProcessDefinition> deploymentList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).list();
		for(ProcessDefinition pd :deploymentList){
			System.out.println(pd.getId()+"====="+pd.getName());
		}
	}
	
	
	//创建一个流程实例
	private static void createFlowInstance(String processDefinitionKey){
		runtimeService.startProcessInstanceByKey(processDefinitionKey);
	}
	
	//获取流程实例
	private static ProcessInstance getFlowInstance(String processInstanceId){
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		System.out.println(instance.getProcessDefinitionKey()+"===="+instance.getProcessInstanceId());
		return instance;
	}
	
	
	//获取当前用户个人任务
	private static List<Task> getTaskByPerson(String assignee){
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).list();
		return taskList;
	}
	//执行流程实例
	private static void completeTask(String taskId){
		taskService.complete(taskId);
	}
	
	
	public static void main(String[] args) {
		try {
			//初始化流程引擎
			initProcessEngine();
				deployFlow();
			String key = "leave";
			getDeployment(key);
			getInstanceDefine(key);
			
			/*//启动流程实例
			String processDefinitionKey = "leave";
			createFlowInstance(processDefinitionKey);*/
			
			/*//查询该流程实例
			String processInstanceId = "5001";
			ProcessInstance instance = getFlowInstance(processInstanceId);*/
			
			/*//查询个人任务
			String assignee = "李四";
			List<Task> taskList = getTaskByPerson(assignee);
			for(Task t : taskList){
				System.out.println("taskId:"+t.getId()+"  taskName:"+t.getName());
			}*/
			
			/*//执行个人任务
			String taskId = "7502";
			completeTask(taskId);*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
