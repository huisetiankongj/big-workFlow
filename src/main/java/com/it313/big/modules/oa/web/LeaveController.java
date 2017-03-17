package com.it313.big.modules.oa.web;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.it313.big.common.web.ActAbstractController;
import com.it313.big.modules.oa.entity.Leave;
import com.it313.big.modules.oa.service.LeaveService;

public class LeaveController extends ActAbstractController{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected LeaveService leaveService;
	
	@RequiresPermissions("oa:leave:view")
	@RequestMapping(value = {"form"})
	public String form(Leave leave, Model model) {
		model.addAttribute("leave", leave);
		return "modules/oa/leaveForm";
	}
	
	/**
	 * 启动请假流程
	 * @param leave	
	 */
	@RequiresPermissions("oa:leave:edit")
	@RequestMapping(value = "save")
	public String save(Leave leave,RedirectAttributes redirectAttributes) {
		try {
			Map<String, Object> variables = Maps.newHashMap();
			leaveService.save(leave, variables);
			addMessage(redirectAttributes,"流程已启动，流程ID：" + leave.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("启动请假流程失败：", e);
			addMessage(redirectAttributes, "系统内部错误！");
		}
		return "redirect:" + adminPath + "/oa/leave/form";
	}
	
}
