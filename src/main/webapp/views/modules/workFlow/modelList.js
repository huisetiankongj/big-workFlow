$(function() {
	var processDefinitionSvc = {
			url: {
				list: rootPath + "/workFlow/process/list?t="+new Date().getTime(),
				del: rootPath + "/workFlow/process/delete?t="+new Date().getTime(),
				updateState :rootPath + "/workFlow/process/updateState?t="+new Date().getTime()
			},
			fnDeploy: function(){
				
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
					url: url?url:processDefinitionSvc.url.processDefinitionForm+(info?('&id='+info.id):''),
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
	var aButtons =[
	  		        { sExtends: "tiny", sButtonClass:"btn btn-success btn-sm hide", sButtonText: "部署", fnClick: function(nButton, oConfig) {
			         	 	processDefinitionSvc.fnSave();
			        }}
			       ];
		             
	//---------------------------------------流程定义列表------------------------------------------------
	var processDefinitionTable = $('#datatables_processDefinition').dataTable({
			sAjaxSource: processDefinitionSvc.url.list,
			fnServerData: fnServer(),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "id", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "deploymentId", sTitle: "DeploymentId"},
					{ aTargets: [ 2 ], mDataProp: "deploymentName", sTitle: "名称"},
					{ aTargets: [ 3 ], mDataProp: "key", sTitle: "KEY"},
					{ aTargets: [ 4 ], mDataProp: "version", sTitle: "版本号"},
					{ aTargets: [ 5 ], mDataProp: "resourceName", sTitle: "XML"},
					{ aTargets: [ 6 ], mDataProp: "diagramResourceName", sTitle: "图片"},
					{ aTargets: [ 7 ], mDataProp: "suspended",sTitle: "是否挂起",mData:function(data){
						var spanClass= "label-success",
							spanText = "挂起",
							state = "0";
						if(data.suspended){
							spanClass = "label-danger";
							spanText = "激活";
							state = "1";
						}	
							
						return '<span class="activeA label ticket-label '+spanClass+'" data-state="'+state+'" data-id="'+data.id+'">'+spanText+'</span>';
					}},
					{ aTargets: [ 8 ], mDataProp: "deploymentTime",sTitle: "部署时间"},
					{ aTargets: [ 9 ], sTitle: "操作",mData:function(data){
						var buttons = [];
						$.each(processDefinitionTable.permission,function(i,perm){
							switch(perm){
							case 'xfjk:doctor:edit':
								buttons.push('<a class="Item-Del" href="javascript:;"><i class="fa fa-del"></i>删除</a>');
								buttons.push('<a class="Item-Convert" href="javascript:;"><i class="fa fa-del"></i>转换为Model</a>');
  								break;
							case 'permission:all':
								buttons.push('<a class="Item-Del" href="javascript:;"><i class="fa fa-del"></i>删除</a>');
								buttons.push('<a class="Item-Convert" href="javascript:;"><i class="fa fa-del"></i>转换为Model</a>');
							}
						});
						return buttons.join('&nbsp;&nbsp;');
					}}
				],
			oTableTools: {
				sRowSelect: "single",
				fnRowSelected:function(){
				},
				aButtons: aButtons
			},
			initComplete: function( settings ){
			},
			drawCallback: function( settings ){
				$(".Item-Del").click(function(){
					var info = processDefinitionTable.fnGetData($(this).parents("tr"));
					layer.confirm('确定删除已勾选的流程吗？', function(index){
						layer.close(index);
			        	processDefinitionSvc.fnDelete(info);
					});
				});
				
				$(".activeA").click(function(){
					var _this = $(this),
						id = _this.attr("data-id"),
						state = _this.attr("data-state");
					processDefinitionSvc.fnActive(id,state);	
				})
			}
		});
		
	processDefinitionSvc.fnRegisterEvent();
});