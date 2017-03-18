package com.it313.big.modules.oa.dao;

import com.it313.big.common.persistence.CrudDao;
import com.it313.big.common.persistence.annotation.MyBatisDao;
import com.it313.big.modules.oa.entity.Leave;

@MyBatisDao
public interface LeaveDao extends CrudDao<Leave>{

	void updateProcessInstanceId(Leave leave);

}
