<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<form name="leaveForm" action="javascript:void(0);" id="leaveForm" class="form form-horizontal">
	<input type="hidden" name="act.taskId" value="${leave.act.taskId}"/>
	<input type="hidden" name="act.procInsId" value="${leave.act.procInsId}"/>
	<input type="hidden" id="flag" name="act.flag"/>
 	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">类型：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			${leave.leaveType}
		</div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">开始时间：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
    		${leave.startTime}
		</div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">结束时间：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
    		${leave.endTime}
		</div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">请假原因：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
    	${leave.reason}
		</div>
	</div>
	<div class="form-group normal-form">
    	<label class="col-xs-12 col-sm-2 control-label  pl-0 pr-5">我的意见：</label>
    	<div class="col-xs-12 col-sm-7 pl-0">
			<textarea id="comment" name="act.comment" ></textarea>
		</div>
    	<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
	<div class="form-group normal-form">
		<label class="col-xs-12 col-sm-2 control-label pl-0 pr-5"></label>
		<div class="col-xs-12 col-sm-7 pl-0">
			<button id="formAgressBtn" class="btn btn-info">同意</button>
			<button id="formRejectBtn" class="btn btn-info">驳回</button>
			<input id="formCancelBtn" class="btn btn-default" type="button" value="取消">
		</div>
		<div class="col-xs-12 col-sm-3 valid-msg"></div>
	</div>
</form>
<table id="datatables_histoicFlow" class="display table-bordered" cellspacing="0"></table>
<script>
	$(function(){
		var leaveFormSvc = {
			url: {
				save : rootPath + "/oa/leave/save?t="+new Date().getTime(),
				findHistoicFlow: rootPath + "/act/task/findHistoicFlow?t="+new Date().getTime(),
				auditSave:rootPath+ "/oa/leave/saveAudit?t="+new Date().getTime(),
			},
			fnCommit: function(){
				var leaveJson = Svc.formToJson($("#leaveForm"));
	        	Svc.AjaxForm.post(leaveFormSvc.url.auditSave,leaveJson,function(response){
	        		if(response == true){
	        			layer.alert('审核成功！',function(index){
	        				layer.close(index);
	        			});
	        		}
	        	});
			}
		}
		// 取消按钮
		$('#formCancelBtn').click(function(){
			API.fnHideForm();
		});
		// 同意
		$('#formAgressBtn').click(function(){
			$("#flag").val("yes");
			leaveFormSvc.fnCommit();
		});
		//驳回
		$('#formSaveBtn').click(function(){
			$("#flag").val("no");
			leaveFormSvc.fnCommit();
		});
		
		var histoicFlowTable = $('#datatables_histoicFlow').dataTable({
			sAjaxSource: leaveFormSvc.url.findHistoicFlow,
			fnServerData: fnServer({"procInsId":"${leave.act.procInsId}"}),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "histIns.id", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "histIns", sTitle: "执行环节",mRender:function(v){
						return v.activityName;
					}},
					{ aTargets: [ 2 ], mDataProp: "assigneeName", sTitle: "执行人"},
					{ aTargets: [ 3 ], mDataProp: "histIns", sTitle: "开始时间",mRender:function(v){
						return v.startTime;
					}},
					{ aTargets: [ 4 ], mDataProp: "histIns", sTitle: "结束时间",mRender:function(v){
						return v.endTime?v.endTime:"";
					}},
					{ aTargets: [ 5 ], mDataProp: "comment", sTitle: "提交意见"},
					{ aTargets: [ 6 ], mDataProp: "durationTime",sTitle: "任务历时"}
				],
			oTableTools: {
				sRowSelect: "single",
				fnRowSelected:function(){
				},
				aButtons: []
			},
			initComplete: function( settings ){
			},
			drawCallback: function( settings ){
			}
		});
		
	})
</script>
