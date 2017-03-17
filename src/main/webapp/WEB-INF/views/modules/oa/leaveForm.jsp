<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假管理</title>
	<script type="text/javascript">
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/leave/">待办任务</a></li>
		<li><a href="${ctx}/oa/leave/list">所有任务</a></li>
		<shiro:hasPermission name="oa:leave:edit"><li class="active"><a href="${ctx}/oa/leave/form">请假申请</a></li></shiro:hasPermission>
	</ul>
<form name="userForm" action="javascript:void(0);" id="userForm" class="form form-horizontal">
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">头像：</label>
    	<div id="uploader" style="position:relative" class="col-xs-12 col-sm-7 pl-0">	
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">工号：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			<input type="text" name="no" id="no" value="${user.no }" placeholder="用户工号" class="form-control input-text" datatype="*1-50" nullmsg="工号不能为空！"/>
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">类型：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			<select name="leaveType" class="form-control select2" datatype="*" nullmsg="类型不能为空！">
				<option value="1">病假</option>
				<option value="2">年假</option>
				<option value="3">产假</option>
			</select>
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">开始时间：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class="Wdate form-control input-text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">结束时间：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="20" class="Wdate form-control input-text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">请假原因：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			<textarea id="reason" name="reason" ></textarea>
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	
	<div class="form-group normal-form">
		<label class="col-xs-12 col-sm-2 control-label pl-0 pr-5"></label>
		<div class="col-xs-12 col-sm-7 pl-0">
			<shiro:hasPermission name="sys:user:edit">
				<button id="formSaveBtn" class="btn btn-info">提交</button>
				<input class="btn btn-success" type="reset" value="重置">
				<input id="formCancelBtn" class="btn btn-default" type="button" value="取消">
			</shiro:hasPermission>
		</div>
	<div class="col-xs-12 col-sm-3 valid-msg"></div>
</form>
</body>
</html>
