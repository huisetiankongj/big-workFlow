$(function() {
	var taskSvc = {
			url: {
				list: rootPath + "/act/task/findToDoList?t="+new Date().getTime(),
				form : rootPath + "/oa/leave/task?t="+new Date().getTime(),
			},
			fnTask: function(info){
				taskSvc.fntaskModal('部署流程信息',info);
			},
			fntaskModal: function(title,info,url){
				API.fnShowForm({
					url: url?url:taskSvc.url.form+(info?('&id='+info.id):'')+(info?('&proDefId='+info.proDefId):'')+(info?('&taskDefinitionKey='+info.taskDefinitionKey):''),
					title: title
				});
			},
			fnRegisterEvent: function(){
				//搜索按钮
				$("#searchBtn").click(function(){
					var params = Svc.formToJson($("#taskSearchForm")),hospital={};
					var hospitalId=$("#hospitalId").val();
					hospitalId&&(hospital.id = hospitalId);
					hospital.id&&(params.hospital = hospital);
					//资料修改审核
					if(params.state=='11'){
						delete params.state;
						params.editState='1';
					}
					taskTable.reDrawParams = params;
					taskTable.fnDraw();
				});
				
				$("#taskSearchForm select.select2").select2();
				//重置按钮
				$("#resetBtn").click(function(){
					Svc.resetForm($("#taskSearchForm"));
				});
			}
	}
	//---------------------------------------待办列表------------------------------------------------
	var taskTable = $('#datatables_task').dataTable({
			sAjaxSource: taskSvc.url.list,
			fnServerData: fnServer(),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "id", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "name", sTitle: "标题",mRender:function(v){
						return v?v:"无";
					}},
					{ aTargets: [ 2 ], mDataProp: "name", sTitle: "当前环节"},
					{ aTargets: [ 3 ], mDataProp: "description", sTitle: "任务内容",mRender:function(v){
						return v?v:"无";
					}},
					{ aTargets: [ 4 ], mDataProp: "proDefName", sTitle: "流程名称"},
					{ aTargets: [ 5 ], mDataProp: "proDefVersion", sTitle: "流程版本"},
					{ aTargets: [ 6 ], mDataProp: "createTime",sTitle: "创建时间"},
					{ aTargets: [ 7 ], sTitle: "操作",mData:function(data){
						var buttons = [];
						buttons.push('<a class="Item-task" href="javascript:;" data-id="'+data.id+'" data-taskDefinitionKey="'+data.taskDefinitionKey+'" data-proDefId="'+data.proDefId+'"><i class="fa fa-del"></i>办理</a>');
						return buttons.join('&nbsp;&nbsp;');
					}}
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
				$(".Item-task").click(function(){
					var _this = $(this),info={},
						id = _this.attr("data-id"),
						proDefId = _this.attr("data-proDefId"),
						taskDefinitionKey = _this.attr("data-taskDefinitionKey");
					info.id = id;
					info.proDefId = proDefId;	
					info.taskDefinitionKey = taskDefinitionKey;	
					taskSvc.fnTask(info);	
				})
			}
		});
		
	taskSvc.fnRegisterEvent();
});