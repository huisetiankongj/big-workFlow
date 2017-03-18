package com.it313.big.modules.oa.entity;

import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import com.it313.big.common.persistence.DataEntity;

public class Leave extends DataEntity<Leave>{

	private static final long serialVersionUID = 1L;
	private String reason; 	// 请假原因
	private String processInstanceId; // 流程实例编号
	private String startTime;	// 请假开始日期
	private String endTime;	// 请假结束日期
	private String realityStartTime;	// 实际开始时间
	private String realityEndTime;	// 实际结束时间
	private String leaveType;	// 假种
	
	private Map<String, Object> variables;
	// 运行中的流程实例
	private ProcessInstance processInstance;
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRealityStartTime() {
		return realityStartTime;
	}
	public void setRealityStartTime(String realityStartTime) {
		this.realityStartTime = realityStartTime;
	}
	public String getRealityEndTime() {
		return realityEndTime;
	}
	public void setRealityEndTime(String realityEndTime) {
		this.realityEndTime = realityEndTime;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	
	
}
