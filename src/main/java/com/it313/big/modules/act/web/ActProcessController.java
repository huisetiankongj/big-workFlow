package com.it313.big.modules.act.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.it313.big.common.persistence.Page;
import com.it313.big.common.persistence.paginate.ThreadLocalActPaginate;
import com.it313.big.common.utils.StringUtils;
import com.it313.big.common.web.BaseController;
import com.it313.big.modules.act.service.ActProcessService;

/**
 * 流程定义相关Controller
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/act/process")
public class ActProcessController extends BaseController {

	@Autowired
	private ActProcessService actProcessService;

	@RequestMapping(value = {"listPage"})
	public Object listPage(){
		return "modules/act/actProcessList";
	}
	/**
	 * 流程定义列表
	 */
	@RequestMapping(value = {"list"})
	@ResponseBody
	public Object list(@RequestBody Map<String,Object> params){
		ThreadLocalActPaginate.set(params);
		List<Map<String,Object>> list = actProcessService.processList(params);
		Object rs = ThreadLocalActPaginate.get();
		if(rs != null)
			return rs;
		return list;
	}
	
	@RequestMapping(value = {"runlistPage"})
	public Object runlistPage(){
		return "modules/act/actProcessRunningList";
	}
	/**
	 * 运行中的实例列表
	 */
	@RequestMapping(value = {"runlist"})
	@ResponseBody
	public Object runlist(@RequestBody Map<String,Object> params){
		ThreadLocalActPaginate.set(params);
		List<Map<String,Object>> list = actProcessService.runningList(params);
		Object rs = ThreadLocalActPaginate.get();
		if(rs != null)
			return rs;
		return list;
	}

	/**
	 * 读取资源，通过部署ID
	 * @param processDefinitionId  流程定义ID
	 * @param processInstanceId 流程实例ID
	 * @param resourceType 资源类型(xml|image)
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "resource/read")
	public void resourceRead(String procDefId, String proInsId, String resType, HttpServletResponse response) throws Exception {
		InputStream resourceAsStream = actProcessService.resourceRead(procDefId, proInsId, resType);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 部署流程
	 */
	@RequestMapping(value = "/deploy", method=RequestMethod.GET)
	public String deploy(Model model) {
		return "modules/act/actProcessDeploy";
	}
	
	/**
	 * 部署流程 - 保存
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/deploy", method=RequestMethod.POST)
	public String deploy(@Value("#{APP_PROP['activiti.export.diagram.path']}") String exportDir, 
			String category, MultipartFile file, RedirectAttributes redirectAttributes) {

		String fileName = file.getOriginalFilename();
		
		if (StringUtils.isBlank(fileName)){
			redirectAttributes.addFlashAttribute("message", "请选择要部署的流程文件");
		}else{
			String message = actProcessService.deploy(exportDir, category, file);
			redirectAttributes.addFlashAttribute("message", message);
		}

		return "redirect:" + adminPath + "/act/process";
	}
	
	/**
	 * 设置流程分类
	 */
	@RequestMapping(value = "updateCategory")
	public String updateCategory(String procDefId, String category, RedirectAttributes redirectAttributes) {
		actProcessService.updateCategory(procDefId, category);
		return "redirect:" + adminPath + "/act/process";
	}

	/**
	 * 挂起、激活流程实例
	 */
	@RequestMapping(value = "update/{state}")
	public String updateState(@PathVariable("state") String state, String procDefId, RedirectAttributes redirectAttributes) {
		String message = actProcessService.updateState(state, procDefId);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:" + adminPath + "/act/process";
	}
	
	/**
	 * 将部署的流程转换为模型
	 * @param procDefId
	 * @param redirectAttributes
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@RequestMapping(value = "convert/toModel")
	public String convertToModel(String procDefId, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException, XMLStreamException {
		org.activiti.engine.repository.Model modelData = actProcessService.convertToModel(procDefId);
		redirectAttributes.addFlashAttribute("message", "转换模型成功，模型ID="+modelData.getId());
		return "redirect:" + adminPath + "/act/model";
	}
	
	/**
	 * 导出图片文件到硬盘
	 */
	@RequestMapping(value = "export/diagrams")
	@ResponseBody
	public List<String> exportDiagrams(@Value("#{APP_PROP['activiti.export.diagram.path']}") String exportDir) throws IOException {
		List<String> files = actProcessService.exportDiagrams(exportDir);;
		return files;
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * @param deploymentId 流程部署ID
	 */
	@RequestMapping(value = "delete")
	public String delete(String deploymentId) {
		actProcessService.deleteDeployment(deploymentId);
		return "redirect:" + adminPath + "/act/process";
	}
	
	/**
	 * 删除流程实例
	 * @param procInsId 流程实例ID
	 * @param reason 删除原因
	 */
	@RequestMapping(value = "deleteProcIns")
	public String deleteProcIns(String procInsId, String reason, RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(reason)){
			addMessage(redirectAttributes, "请填写删除原因");
		}else{
			actProcessService.deleteProcIns(procInsId, reason);
			addMessage(redirectAttributes, "删除流程实例成功，实例ID=" + procInsId);
		}
		return "redirect:" + adminPath + "/act/process/running/";
	}
	
}