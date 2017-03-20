package com.it313.big.modules.act.entity;

import java.util.Date;

import com.it313.big.common.entity.ActBaseEntity;

public class SelfTask  extends ActBaseEntity<SelfTask>{

private static final long serialVersionUID = 1L;
	
	private String id;//任务id
	private String name;
	private String taskDefinitionKey;
	private Date createTime;
	private String description;
	private String assignee;
	
	
	private String proDefId;//定义id
	private String proDefName;//定义名称
	private int proDefVersion;//定义版本
	
	private String processInstanceId;//实例id
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProDefName() {
		return proDefName;
	}
	public void setProDefName(String proDefName) {
		this.proDefName = proDefName;
	}
	public int getProDefVersion() {
		return proDefVersion;
	}
	public void setProDefVersion(int proDefVersion) {
		this.proDefVersion = proDefVersion;
	}
	public String getProDefId() {
		return proDefId;
	}
	public void setProDefId(String proDefId) {
		this.proDefId = proDefId;
	}
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	
	
}
