<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<form name="leaveForm" action="javascript:void(0);" id="leaveForm" class="form form-horizontal">
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
			<button id="formSaveBtn" class="btn btn-info">提交</button>
			<input class="btn btn-success" type="reset" value="重置">
			<input id="formCancelBtn" class="btn btn-default" type="button" value="取消">
		</div>
		<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
</form>
<script>
	$(function(){
		var leaveFormSvc = {
			url: {
				save : rootPath + "/oa/leave/save?t="+new Date().getTime()
			},
			fnCommit: function(){
				var leaveJson = Svc.formToJson($("#leaveForm"));
	        	Svc.AjaxJson.post(leaveFormSvc.url.save,leaveJson,function(response){
	        		if(response == true){
	        			layer.alert('保存成功！',function(index){});
	        		}
	        	});
			}
		}
		// 取消按钮
		$('#formCancelBtn').click(function(){
			API.fnHideForm();
		});
		// 取消按钮
		$('#formSaveBtn').click(function(){
			leaveFormSvc.fnCommit();
		});
		
		
		
	})
</script>