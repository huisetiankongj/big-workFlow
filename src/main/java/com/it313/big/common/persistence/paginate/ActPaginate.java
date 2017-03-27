package com.it313.big.common.persistence.paginate;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import com.github.pagehelper.Page;
import com.it313.big.modules.sys.utils.UserUtils;

public class ActPaginate<T> implements Serializable {
	private static final long serialVersionUID = 3119497648435553085L;
	/**
	 * 每页多少行
	 */
	private int rowsOfPage;
	/**
	 * 当前页，从1开始
	 */
	private int currentPage;
	/**
	 * 总页数，在总记录数提供后计算得出
	 */
	private int totalPages;
	/**
	 * 总记录数
	 */
	private long totalRows;
	/**
	 * 当前页的最小记录号，从1开始
	 */
	private int minRowNumber;
	/**
	 * 当前页的最大记录号，与rowsOfPage和totalRows有关
	 */
	private int maxRowNumber;
	
	/**
	 * 第一行num（用于其他直接首尾行查询）
	 */
	private int startRowNum;
	
	/**
	 * 最后一行（用于其他直接首尾行查询）
	 */
	private int lastRowNum;
	/**
	 * 结果集，查询后得到
	 */
	private List<T> datas;
	/**
	 * 当前用户操作权限
	 */
	private Set<String> permission;
	/**
	 * 菜单id
	 */
	private String menuId;
	//当前页的数量 <= rowsOfPage，该属性来自ArrayList的size属性
    private int size;
	
	/**
	 * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
	 * 而出现一些问题。
	 * @param list          page结果
	 * @param navigatePages 页码数量
	 */
	public ActPaginate(List list,int curPage,int pageSize, int totalRow,String menuId) {
		this.currentPage = curPage;
		this.rowsOfPage = pageSize;
		this.totalRows = totalRow;
		this.totalPages = totalRow%pageSize==0?totalRow/pageSize:totalRow/pageSize+1;
		this.datas = list;
		this.size = list.size();
		this.permission = UserUtils.getCurrentPermission(menuId);	
	}

	public void setPage(org.activiti.engine.query.Query query) {
		int totalRow = (int) query.count();
		List list = query.listPage(this.startRowNum, this.lastRowNum);
		this.totalRows = totalRow;
		this.totalPages = totalRow%this.rowsOfPage==0?totalRow/this.rowsOfPage:totalRow/this.rowsOfPage+1;
		this.datas = list;
		this.size = list.size();
		this.permission = UserUtils.getCurrentPermission(menuId);	
	}
	
	
	public ActPaginate() {
		this.rowsOfPage = -1;
	}

	public int getRowsOfPage() {
		return rowsOfPage;
	}

	public void setRowsOfPage(int rowsOfPage) {
		this.rowsOfPage = rowsOfPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public int getMinRowNumber() {
		return minRowNumber;
	}

	public void setMinRowNumber(int minRowNumber) {
		this.minRowNumber = minRowNumber;
	}

	public int getMaxRowNumber() {
		return maxRowNumber;
	}

	public void setMaxRowNumber(int maxRowNumber) {
		this.maxRowNumber = maxRowNumber;
	}

	public List getDatas() {
		return datas;
	}

	public void setDatas(List datas) {
		this.datas = datas;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	
	public int getStartRowNum() {
		return startRowNum;
	}
	public void setStartRowNum(int startRowNum) {
		this.startRowNum = startRowNum;
	}
	public int getLastRowNum() {
		return lastRowNum;
	}
	public void setLastRowNum(int lastRowNum) {
		this.lastRowNum = lastRowNum;
	}
	
	public Set<String> getPermission() {
		return permission;
	}

	public void setPermission(Set<String> permission) {
		this.permission = permission;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
}
