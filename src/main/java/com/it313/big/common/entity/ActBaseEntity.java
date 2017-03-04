package com.it313.big.common.entity;

import java.io.Serializable;

import com.it313.big.common.persistence.paginate.Paginate;

public class ActBaseEntity<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected Paginate<T> paginate;

	public Paginate<T> getPaginate() {
		if (paginate == null){
			paginate = new Paginate<T>();
		}
		return paginate;
	}
	
	public Paginate<T> setPage(Paginate<T> paginate) {
		this.paginate = paginate;
		return paginate;
	}
}
