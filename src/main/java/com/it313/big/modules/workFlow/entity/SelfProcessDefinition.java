package com.it313.big.modules.workFlow.entity;

import java.util.Date;

import com.it313.big.common.entity.ActBaseEntity;


public class SelfProcessDefinition extends ActBaseEntity<SelfProcessDefinition>{

	private static final long serialVersionUID = 1L;
	
	private String id;//流程定义id
	private String name;//定义名称
	private String key;//流程key
	private int version;//流程版本号
	private String category;//
	private String deploymentId;//部署id
	private String deploymentName;//部署名称
	private Date deploymentTime;//部署时间
	private String resourceName;//资源名称
	private String tenantId;
	private Integer historyLevel;
	private String diagramResourceName;
	private boolean suspended;//是否挂起
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getDeploymentName() {
		return deploymentName;
	}
	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public Integer getHistoryLevel() {
		return historyLevel;
	}
	public void setHistoryLevel(Integer historyLevel) {
		this.historyLevel = historyLevel;
	}
	public String getDiagramResourceName() {
		return diagramResourceName;
	}
	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	public Date getDeploymentTime() {
		return deploymentTime;
	}
	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}
	
}
