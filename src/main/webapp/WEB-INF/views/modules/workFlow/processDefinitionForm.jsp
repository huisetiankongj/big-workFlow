<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<form name="processDefinitionForm" action="javascript:void(0);" id="processDefinitionForm" class="form form-horizontal">
	<input type="hidden" id="id" name="processDefinition.id" value="${processDefinition.id }">
	<div class="form-group normal-form xgb">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">部署名称：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			<input type="text" name="name" id="name" value="${name }" placeholder="部署名称" class="form-control input-text" datatype="*1-50" nullmsg="部署名称不能为空！"/>
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	<div class="form-group normal-form xgb">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">上传文件：</label>
    	<div id="uploader" style="position:relative" class="col-xs-12 col-sm-7 pl-0">	
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	
	<div class="form-group normal-form">
		<label class="col-xs-12 col-sm-2 control-label pl-0 pr-5"></label>
		<div class="col-xs-12 col-sm-7 pl-0">
			<button id="formSaveBtn" class="btn btn-info detail">部署</button>
			<input id="formCancelBtn" class="btn btn-default" type="button" value="取消">
		</div>
	<div class="col-xs-12 col-sm-3 valid-msg"></div>
</div>
</form>
<script type="text/javascript" src="${rootPath}/static/org/it313/city/areaselect.js"></script>
<script type="text/javascript" src="${rootPath}/views/modules/workFlow/processDefinitionForm.js"></script>