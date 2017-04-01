$(function() {
	var modelSvc = {
			url: {
				list: rootPath + "/act/model/findModelList?t="+new Date().getTime(),
				form : rootPath + "/oa/leave/form?t="+new Date().getTime(),
				resource:rootPath + "/act/process/resource/read?t="+new Date().getTime()
			},
			fnStart:function(){
				modelSvc.fnmodelModal('请假申请');
			},
			fnmodelModal: function(title,info,url){
				API.fnShowForm({
					url: url?url:modelSvc.url.form+(info?('&id='+info.id):''),
					title: title
				});
			},
			fnRegisterEvent: function(){
				//搜索按钮
				$("#searchBtn").click(function(){
					var params = Svc.formToJson($("#modelSearchForm")),hospital={};
					var hospitalId=$("#hospitalId").val();
					hospitalId&&(hospital.id = hospitalId);
					hospital.id&&(params.hospital = hospital);
					//资料修改审核
					if(params.state=='11'){
						delete params.state;
						params.editState='1';
					}
					modelTable.reDrawParams = params;
					modelTable.fnDraw();
				});
				
				$("#modelSearchForm select.select2").select2();
				//重置按钮
				$("#resetBtn").click(function(){
					Svc.resetForm($("#modelSearchForm"));
				});
			}
	}
	//---------------------------------------流程定义列表------------------------------------------------
	var modelTable = $('#datatables_model').dataTable({
			sAjaxSource: modelSvc.url.list,
			fnServerData: fnServer(),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "id", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "category", sTitle: "流程分类",mRender:function(v){
						return v?v:"无";
					}},
					{ aTargets: [ 2 ], mDataProp: "id", sTitle: "模型ID"},
					{ aTargets: [ 3 ], mDataProp: "key", sTitle: "模型标识"},
					{ aTargets: [ 4 ], mDataProp: "name", sTitle: "模型名称"},
					{ aTargets: [ 5 ], mDataProp: "version", sTitle: "版本号"},
					{ aTargets: [ 6 ], mDataProp: "createTime",sTitle: "创建时间"},
					{ aTargets: [ 7 ], mDataProp: "lastUpdateTime",sTitle: "最后更新时间"},
					{ aTargets: [ 8 ], sTitle: "操作",mData:function(data){
						var buttons = [];
						buttons.push('<a href="'+rootPath+'/act/process-editor/modeler.jsp?modelId=${model.id}" target="_blank">编辑</a>')
						buttons.push('<a href="'+rootPath+'/act/model/deploy?id='+data.id+'" target="_blank">部署</a>')
						buttons.push('<a href="'+rootPath+'/act/model/export?id='+data.id+'" target="_blank">导出</a>')
						buttons.push('<a href="'+rootPath+'/act/model/delete?id='+data.id+'" target="_blank">删除</a>')
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
					modelSvc.fnStart(id);	
				})
			}
		});
		
	modelSvc.fnRegisterEvent();
});