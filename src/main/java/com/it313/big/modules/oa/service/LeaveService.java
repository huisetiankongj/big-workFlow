package com.it313.big.modules.oa.service;

import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it313.big.common.service.ActAbstractService;
import com.it313.big.common.utils.StringUtils;
import com.it313.big.modules.act.utils.ActUtils;
import com.it313.big.modules.oa.dao.LeaveDao;
import com.it313.big.modules.oa.entity.Leave;

@Service
@Transactional(readOnly = true)
public class LeaveService extends ActAbstractService {

	@Autowired
	private LeaveDao leaveDao;
	
	/**
	 * 启动流程
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(Leave leave, Map<String, Object> variables) {
		// 保存业务数据
		if (StringUtils.isBlank(leave.getId())){
			leave.preInsert();
			leaveDao.insert(leave);
		}else{
			leave.preUpdate();
			leaveDao.update(leave);
		}
		logger.debug("save entity: {}", leave);
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(leave.getCurrentUser().getLoginName());
		
		// 启动流程
		String businessKey = ActUtils.PD_LEAVE[0]+":"+leave.getId();
		variables.put("type", "leave");
		variables.put("busId", businessKey);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActUtils.PD_LEAVE[0], businessKey, variables);
		leave.setProcessInstance(processInstance);
		
		// 更新流程实例ID
		leave.setProcessInstanceId(processInstance.getId());
		leaveDao.updateProcessInstanceId(leave);
		
		logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { 
				ActUtils.PD_LEAVE[0], businessKey, processInstance.getId(), variables });
		
	}

	public Leave get(String id) {
		return leaveDao.get(id);
	}
		
}
