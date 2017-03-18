$(function() {
	var processDefinitionSvc = {
			url: {
				list: rootPath + "/workFlow/process/list?t="+new Date().getTime(),
				del: rootPath + "/workFlow/process/delete?t="+new Date().getTime(),
				form : rootPath + "/workFlow/process/form?t="+new Date().getTime(),
				updateState :rootPath + "/workFlow/process/updateState?t="+new Date().getTime()
			},
			fnDeploy: function(){
				processDefinitionSvc.fnprocessDefinitionModal('閮ㄧ讲娴佺▼淇℃伅');
			},
			fnDelete: function(info){
				Svc.AjaxJson.post(processDefinitionSvc.url.del+"&deploymentId="+info.deploymentId,{},function(response){
					if(response){
						layer.msg('鎿嶄綔鎴愬姛锛�);
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
						layer.msg('鎿嶄綔鎴愬姛锛�);
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
				//鎼滅储鎸夐挳
				$("#searchBtn").click(function(){
					var params = Svc.formToJson($("#processDefinitionSearchForm")),hospital={};
					var hospitalId=$("#hospitalId").val();
					hospitalId&&(hospital.id = hospitalId);
					hospital.id&&(params.hospital = hospital);
					//璧勬枡淇敼瀹℃牳
					if(params.state=='11'){
						delete params.state;
						params.editState='1';
					}
					processDefinitionTable.reDrawParams = params;
					processDefinitionTable.fnDraw();
				});
				
				$("#processDefinitionSearchForm select.select2").select2();
				//閲嶇疆鎸夐挳
				$("#resetBtn").click(function(){
					Svc.resetForm($("#processDefinitionSearchForm"));
				});
			}
	}
	var aButtons =[
	  		        { sExtends: "tiny", sButtonClass:"btn btn-success btn-sm", sButtonText: "閮ㄧ讲", fnClick: function(nButton, oConfig) {
			         	 	processDefinitionSvc.fnDeploy();
			        }}
			       ];
		             
	//---------------------------------------娴佺▼瀹氫箟鍒楄〃------------------------------------------------
	var processDefinitionTable = $('#datatables_processDefinition').dataTable({
			sAjaxSource: processDefinitionSvc.url.list,
			fnServerData: fnServer(),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "id", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "deploymentId", sTitle: "DeploymentId"},
					{ aTargets: [ 2 ], mDataProp: "deploymentName", sTitle: "鍚嶇О"},
					{ aTargets: [ 3 ], mDataProp: "key", sTitle: "KEY"},
					{ aTargets: [ 4 ], mDataProp: "version", sTitle: "鐗堟湰鍙�},
					{ aTargets: [ 5 ], mDataProp: "resourceName", sTitle: "XML"},
					{ aTargets: [ 6 ], mDataProp: "diagramResourceName", sTitle: "鍥剧墖"},
					{ aTargets: [ 7 ], mDataProp: "suspended",sTitle: "鏄惁鎸傝捣",mData:function(data){
						var spanClass = "label-danger",
							spanText = "鎸傝捣",
							state = "0";
						if(data.suspended){
							spanClass= "label-success";
							spanText = "婵�椿";
							state = "1";
						}	
							
						return '<span class="activeA label ticket-label '+spanClass+'" data-state="'+state+'" data-id="'+data.id+'">'+spanText+'</span>';
					}},
					{ aTargets: [ 8 ], mDataProp: "deploymentTime",sTitle: "閮ㄧ讲鏃堕棿"},
					{ aTargets: [ 9 ], sTitle: "鎿嶄綔",mData:function(data){
						var buttons = [];
						$.each(processDefinitionTable.permission,function(i,perm){
							switch(perm){
							case 'xfjk:doctor:edit':
								buttons.push('<a class="Item-Del" href="javascript:;"><i class="fa fa-del"></i>鍒犻櫎</a>');
								buttons.push('<a class="Item-Convert" href="javascript:;"><i class="fa fa-del"></i>杞崲涓篗odel</a>');
  								break;
							case 'permission:all':
								buttons.push('<a class="Item-Del" href="javascript:;"><i class="fa fa-del"></i>鍒犻櫎</a>');
								buttons.push('<a class="Item-Convert" href="javascript:;"><i class="fa fa-del"></i>杞崲涓篗odel</a>');
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
					layer.confirm('纭畾鍒犻櫎宸插嬀閫夌殑娴佺▼鍚楋紵', function(index){
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