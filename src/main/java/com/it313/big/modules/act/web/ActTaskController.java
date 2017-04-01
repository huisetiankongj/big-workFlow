package com.it313.big.modules.act.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.it313.big.common.persistence.paginate.ThreadLocalActPaginate;
import com.it313.big.common.web.BaseController;
import com.it313.big.modules.act.entity.Act;
import com.it313.big.modules.act.service.ActTaskService;
import com.it313.big.modules.act.utils.ActUtils;

@Controller
@RequestMapping(value = "${adminPath}/act/task")
public class ActTaskController extends BaseController{
	
	@Autowired
	private ActTaskService actTaskService;
	
	/**
	 * 获取流程表单
	 * @param taskId	任务ID
	 * @param taskName	任务名称
	 * @param taskDefKey 任务环节标识
	 * @param procInsId 流程实例ID
	 * @param procDefId 流程定义ID
	 */
	@RequestMapping(value = "form")
	public String form(Act act, HttpServletRequest request, Model model){
		// 获取流程XML上的表单KEY
		String formKey = actTaskService.getFormKey(act.getProcDefId(), act.getTaskDefKey());
		
		formKey ="/oa/leave/leaveTask";
		// 获取流程实例对象
		if (act.getProcInsId() != null){
			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
		}
		
		return "redirect:" + ActUtils.getFormUrl(formKey, act);
		
//		// 传递参数到视图
//		model.addAttribute("act", act);
//		model.addAttribute("formUrl", formUrl);
//		return "modules/act/actTaskForm";
	}
	
	/**
	 * 待办页面
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = {"todo", ""})
	public String todoListPage() throws Exception {
		return "modules/act/actTaskTodoList";
	}
	
	/**
	 * 获取待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "findToDoList")
	@ResponseBody
	public Object findToDoList(@RequestBody Act act) {
		return actTaskService.todoList(act);
	}
	
	
	/**
	 * 获取流转历史列表
	 * @param procInsId 流程实例
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	@RequestMapping(value = "findHistoicFlow")
	@ResponseBody
	public Object findHistoicFlow(@RequestBody Act act, Model model){
		return actTaskService.histoicFlowList(act, "", "");
	}
	
}
