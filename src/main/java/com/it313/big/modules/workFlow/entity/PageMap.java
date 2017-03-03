package com.it313.big.modules.workFlow.entity;

import java.util.HashMap;
import com.it313.big.common.persistence.paginate.Paginate;

public class PageMap extends HashMap{

	private Paginate Paginate;

	public Paginate getPaginate() {
		return Paginate;
	}

	public void setPaginate(Paginate paginate) {
		Paginate = paginate;
	}
	
	
}
