package com.it313.big.common.utils;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Lazy(false)
public class IdGenExt extends IdGen implements IdGenerator{

	@Override
	public String getNextId() {
		return IdGen.uuid();
	}

}

