package com.it313.big.common.entity;

import java.io.Serializable;

import com.it313.big.common.persistence.paginate.ActPaginate;

public class ActBaseEntity<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected ActPaginate<T> paginate;

	public ActPaginate<T> getPaginate() {
		if (paginate == null){
			paginate = new ActPaginate<T>();
		}
		return paginate;
	}
	
	public ActPaginate<T> setPage(ActPaginate<T> paginate) {
		this.paginate = paginate;
		return paginate;
	}
}
