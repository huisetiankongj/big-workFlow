$(function() {
	var processDefinitionSvc = {
			url: {
				list: rootPath + "/act/task/findProcessList?t="+new Date().getTime(),
				form : rootPath + "/oa/leave/form?t="+new Date().getTime(),
				updateState :rootPath + "/workFlow/process/updateState?t="+new Date().getTime()
			},
			fnDeploy: function(){
				processDefinitionSvc.fnprocessDefinitionModal('部署流程信息');
			},
			fnStart:function(){
				processDefinitionSvc.fnprocessDefinitionModal('请假申请');
			},
			fnDelete: function(info){
				Svc.AjaxJson.post(processDefinitionSvc.url.del+"&deploymentId="+info.deploymentId,{},function(response){
					if(response){
						layer.msg('操作成功！');
						processDefinitionTable.fnDraw();
					}else{
						layer.alert(response.join('<br/>'));
					}
				});
			},
			fnActive: function(id,state){
				var url = processDefinitionSvc.url.updateState+"&state="+state+"&processDefinitionId="+id;
				Svc.AjaxForm.post(url,{},function(response){
					if(response){
						layer.msg('操作成功！');
						processDefinitionTable.fnDraw();
					}else{
						layer.alert(response.join('<br/>'));
					}
				});
			},
			fnprocessDefinitionModal: function(title,info,url){
				API.fnShowForm({
					url: url?url:processDefinitionSvc.url.form+(info?('&id='+info.id):''),
					title: title
				});
			},
			fnRegisterEvent: function(){
				//搜索按钮
				$("#searchBtn").click(function(){
					var params = Svc.formToJson($("#processDefinitionSearchForm")),hospital={};
					var hospitalId=$("#hospitalId").val();
					hospitalId&&(hospital.id = hospitalId);
					hospital.id&&(params.hospital = hospital);
					//资料修改审核
					if(params.state=='11'){
						delete params.state;
						params.editState='1';
					}
					processDefinitionTable.reDrawParams = params;
					processDefinitionTable.fnDraw();
				});
				
				$("#processDefinitionSearchForm select.select2").select2();
				//重置按钮
				$("#resetBtn").click(function(){
					Svc.resetForm($("#processDefinitionSearchForm"));
				});
			}
	}
	//---------------------------------------流程定义列表------------------------------------------------
	var processDefinitionTable = $('#datatables_process').dataTable({
			sAjaxSource: processDefinitionSvc.url.list,
			fnServerData: fnServer(),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "id", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "category", sTitle: "流程分类",mRender:function(v){
						v?v:"无";
					}},
					{ aTargets: [ 2 ], mDataProp: "key", sTitle: "流程标识"},
					{ aTargets: [ 3 ], mDataProp: "deploymentName", sTitle: "流程名称"},
					
					{ aTargets: [ 4 ], mDataProp: "diagramResourceName", sTitle: "流程图"},
					{ aTargets: [ 5 ], mDataProp: "version", sTitle: "版本号"},
					{ aTargets: [ 6 ], mDataProp: "deploymentTime",sTitle: "更新时间"},
					{ aTargets: [ 7 ], sTitle: "操作",mData:function(data){
						var buttons = [];
						buttons.push('<a class="Item-start" href="javascript:;" data-id="'+data.id+'"><i class="fa fa-del"></i>启动流程</a>');
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
				$(".Item-start").click(function(){
					var _this = $(this),
						id = _this.attr("data-id"),
						state = _this.attr("data-state");
					processDefinitionSvc.fnStart(id);	
				})
			}
		});
		
	processDefinitionSvc.fnRegisterEvent();
});