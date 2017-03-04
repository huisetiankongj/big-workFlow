<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<table id="datatables_deployment" class="display table-bordered" cellspacing="0"></table>
<script type="text/javascript">
	var url = rootPath + "/workFlow/process/list?t="+new Date().getTime()
	//---------------------------------------发布列表------------------------------------------------
	var deploymentTable = $('#datatables_deployment').dataTable({
			sAjaxSource: url,
			fnServerData: fnServer({"resourceName":"czx"}),
			oDTCheckbox: {
		        iColIndex:0
		    },
			aoColumnDefs: [
					{ aTargets: [ 0 ], mData: "deploymentId", sClass: "text-center", sTitle: "<input type='checkbox' class='TableCheckall'>",bSortable: false, sWidth: "20px"},
					{ aTargets: [ 1 ], mDataProp: "resourceName", sTitle: "定义名称"}
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

</script>
