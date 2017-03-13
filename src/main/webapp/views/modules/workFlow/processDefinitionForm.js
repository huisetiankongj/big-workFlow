$(function() {
	var fileUploader;
	var processDefinitionSvc = {
			url: {
				deploy: rootPath + "/workFlow/process/deploy?t="+new Date().getTime()
			},
			fnDeploy: function(){
			
			}
	};
	
	function registerEvent(){
		// 取消按钮
		$('#formCancelBtn').click(function(){
			API.fnHideForm();
		});
	}
	
	function initPage(){
		registerEvent();
		//附件
		var options = {btns:["picker"],attachment:{type:"workFlow"},isMul:false,isForce:false,acceptExt:'zip,bar,bpmn,bpmn20.xml'}
		fileUploader = new FileUploader("#uploader",options);
	}
	
	
	initPage();
});