package com.it313.big.common.persistence.paginate;

import java.util.List;
import java.util.Map;

import org.activiti.engine.query.Query;

import com.it313.big.modules.sys.utils.UserUtils;


public class ThreadLocalActPaginate {

	public static final String PARAM_START_ROWNUM = "startRowNum";
	public static final String PARAM_LAST_ROWNUM = "lastRowNum";
	public static final String PARAM_ROWS_OF_PAGE = "rowsOfPage";
	public static final String PARAM_CURRENT_PAGE = "currentPage";
	
	private static ThreadLocal<ActPaginate> local =  new ThreadLocal<ActPaginate>();
   
	public static ActPaginate get()
	{
		return local.get();
	}
	
	public static ActPaginate set(ActPaginate page)
	{
		ActPaginate old = local.get();
		local.set(page);
		return old;
	}
	
	public static void remove()
	{
		local.remove();
	}
	
	@SuppressWarnings("unchecked")
	public static void set(Map<String, Object> params,String paramRowsOfPage, String paramCurrentPage, String paramStartRowNum, String paramLastRowNum)
	{
		
		Map<String,Object> paginate = (Map<String, Object>)params.get("paginate");
		Integer startRowNum = Integer.parseInt(paginate.get(paramStartRowNum).toString());
		Integer lastRowNum = Integer.parseInt(paginate.get(paramLastRowNum).toString());
		Integer rowsOfPage = Integer.parseInt(paginate.get(paramRowsOfPage).toString());
		Integer currentPage = Integer.parseInt(paginate.get(paramCurrentPage).toString());
		if(rowsOfPage != null)
		{
			if(currentPage == null)
				currentPage = 1;
			ActPaginate p = new ActPaginate(currentPage,rowsOfPage,startRowNum, lastRowNum);
			set(p);
		}
	}
	
	public static void set(Map<String, Object> params)
	{
		set(params, PARAM_ROWS_OF_PAGE, PARAM_CURRENT_PAGE,PARAM_START_ROWNUM, PARAM_LAST_ROWNUM);
	}
	
	
	public static List<?> execQuery(Query<?, ?> query){
		ActPaginate page = local.get();
		int totalRow = (int) query.count();
		List<?> list = query.listPage(page.getStartRowNum(),page.getLastRowNum());
		page.setTotalRows(totalRow);
		page.setTotalPages(totalRow%page.getRowsOfPage()==0?totalRow/page.getRowsOfPage():totalRow/page.getRowsOfPage()+1); 
		page.setDatas(list);
		page.setSize(list.size());
		page.setPermission(UserUtils.getCurrentPermission(page.getMenuId()));	
		return list;
	}
	public static List<?> execOnlyQuery(Query<?, ?> query){
		ActPaginate page = local.get();
		List<?> list = query.listPage(page.getStartRowNum(),page.getLastRowNum());
		return list;
	}
}
