package com.it313.big.common.entity;

import java.io.Serializable;

import com.it313.big.common.persistence.paginate.ActPaginate;

public class ActBaseEntity<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected ActPaginate paginate;

	public ActPaginate getPaginate() {
		if (paginate == null){
			paginate = new ActPaginate();
		}
		return paginate;
	}
	
	public ActPaginate setPage(ActPaginate paginate) {
		this.paginate = paginate;
		return paginate;
	}
}
