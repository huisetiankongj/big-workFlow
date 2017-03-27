<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<form id="userSearchForm" action="" method="get" class="form form-inline">
	<div class="well well-sm">
		类型<select id="category" name="category" class="input-medium">
			<option value="">全部分类</option>
			<option value="1">类1</option>
			<option value="2">类2</option>
		  </select>
		&nbsp;
		<input type="button" id="searchBtn"  class="btn btn-primary btn-sm" value="搜索" />
		<input type="button" id="resetBtn" class="btn btn-primary btn-sm" value="重置" />
	</div>
</form>
<table id="datatables_model" class="display table-bordered" cellspacing="0"></table>
<script type="text/javascript" src="${rootPath}/views/modules/act/actModelList.js"></script>
