package com.it313.big.modules.act.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.it313.big.common.web.BaseController;
import com.it313.big.modules.act.entity.Act;
import com.it313.big.modules.act.service.ActTaskService;
import com.it313.big.modules.workFlow.entity.SelfProcessDefinition;

@Controller
@RequestMapping(value = "${adminPath}/act/task")
public class ActTaskController extends BaseController{
	
	@Autowired
	private ActTaskService actTaskService;
	
	/**
	 * 流程列表页面
	 * @param category 流程分类
	 */
	@RequestMapping(value = "process/list")
	public Object processList(Model model) {
		return "modules/act/actTaskProcessList";
	}
	
	/**
	 * 流程列表
	 * @param category 流程分类
	 */
	@RequestMapping(value = "findProcessList")
	@ResponseBody
	public Object findProcessList(@RequestBody SelfProcessDefinition pageMap) {
		return actTaskService.processList(pageMap.getPaginate(),pageMap.getCategory());
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
		List<Act> list = actTaskService.todoList(act);
		return list;
	}
	
	
}
