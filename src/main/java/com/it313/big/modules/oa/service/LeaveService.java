package com.it313.big.modules.oa.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it313.big.common.service.BaseService;
import com.it313.big.common.utils.StringUtils;
import com.it313.big.modules.oa.dao.LeaveDao;
import com.it313.big.modules.oa.entity.Leave;

@Service
@Transactional(readOnly = true)
public class LeaveService extends BaseService {

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
	}
		
}
