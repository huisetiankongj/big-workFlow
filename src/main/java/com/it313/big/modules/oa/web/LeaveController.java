package com.it313.big.modules.oa.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.it313.big.common.utils.StringUtils;
import com.it313.big.common.web.BaseController;
import com.it313.big.modules.oa.entity.Leave;
import com.it313.big.modules.oa.service.LeaveService;

@Controller
@RequestMapping(value = "${adminPath}/oa/leave")
public class LeaveController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected LeaveService leaveService;
	
	@ModelAttribute
	public Leave get(@RequestParam(required=false) String id){//, 
//			@RequestParam(value="act.procInsId", required=false) String procInsId) {
		Leave leave = null;
		if (StringUtils.isNotBlank(id)){
			leave = leaveService.get(id);
//		}else if (StringUtils.isNotBlank(procInsId)){
//			testAudit = testAuditService.getByProcInsId(procInsId);
		}
		if (leave == null){
			leave = new Leave();
		}
		return leave;
	}
	
	@RequestMapping(value = {"form"})
	public String form(Leave leave, Model model) {
		model.addAttribute("leave", leave);
		return "modules/oa/leaveForm";
	}
	
	@RequestMapping(value = {"leaveTask"})
	public String leaveTask(Leave leave, Model model) {
		model.addAttribute("leave", leave);
		return "modules/oa/leaveTask";
	}
	
	/**
	 * 启动请假流程
	 * @param leave	
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public Object save(@RequestBody Leave leave,RedirectAttributes redirectAttributes) {
		try {
			Map<String, Object> variables = Maps.newHashMap();
			leaveService.save(leave, variables);
		} catch (Exception e) {
			logger.error("启动请假流程失败：", e);
			return setErrorMsg("启动请假流程失败");
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveAudit")
	@ResponseBody
	public Object saveAudit(Leave leave) {
		if (StringUtils.isBlank(leave.getAct().getFlag())
				|| StringUtils.isBlank(leave.getAct().getComment())){
			
			return setErrorMsg("请填写审核意见");
		}
		leaveService.auditSave(leave);
		return Boolean.TRUE;
	}
	
}
