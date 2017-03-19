$(function() {
	var processDefinitionSvc = {
			url: {
				list: rootPath + "/act/task/findProcessList?t="+new Date().getTime(),
				form : rootPath + "/oa/leave/form?t="+new Date().getTime()
			},
			fnStart:function(){
				processDefinitionSvc.fnprocessDefinitionModal('�������');
			},
			fnprocessDefinitionModal: function(title,info,url){
				API.fnShowForm({
					url: url?url:processDefinitionSvc.url.form+(info?('&id='+info.id):''),
					title: title
				});
			},
			fnRegisterEvent: function(){
				//������ť
				$("#searchBtn").click(function(){
					var params = Svc.formToJson($("#processDefinitionSearchForm")),hospital={};
					var hospitalId=$("#hospitalId").val();
					hospitalId&&(hospital.id = hospitalId);
					hospital.id&&(params.hospital = hospital);
					//�����޸����
					if(params.state=='11'){
						delete params.state;
						params.editState='1';
					}
					processDefinitionTable.reDrawParams = params;
					processDefinitionTable.fnDraw();
				});
				
				$("#processDefinitionSearchForm select.select2").select2();
				//���ð�ť
				$("#resetBtn").click(function(){
					Svc.resetForm($("#processDefinitionSearchForm"));
				});
			}
	}
	//---------------------------------------���̶����б�------------------------------------------------
	var processDefinitionTable = $('#datatables_process').dataTable({
			sAjaxSource: processDefinitionSvc.url.list,
			fnServerData: fnServer(),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "id", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "category", sTitle: "���̷���",mRender:function(v){
						return v?v:"��";
					}},
					{ aTargets: [ 2 ], mDataProp: "key", sTitle: "���̱�ʶ"},
					{ aTargets: [ 3 ], mDataProp: "deploymentName", sTitle: "��������"},
					
					{ aTargets: [ 4 ], mDataProp: "diagramResourceName", sTitle: "����ͼ"},
					{ aTargets: [ 5 ], mDataProp: "version", sTitle: "�汾��"},
					{ aTargets: [ 6 ], mDataProp: "deploymentTime",sTitle: "����ʱ��"},
					{ aTargets: [ 7 ], sTitle: "����",mData:function(data){
						var buttons = [];
						buttons.push('<a class="Item-start" href="javascript:;" data-id="'+data.id+'"><i class="fa fa-del"></i>��������</a>');
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