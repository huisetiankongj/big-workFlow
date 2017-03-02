package com.it313.big.modules.workFlow.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.it313.big.common.utils.StringUtils;
import com.it313.big.modules.sys.entity.Attachment;
import com.it313.big.modules.sys.entity.User;

@Controller
@RequestMapping(value = "${adminPath}/workFlow/")
public class WorkFlowController {

	@RequiresPermissions("sys:workFlow:view")
	@RequestMapping(value = {"deploy/index"})
	public String index(User user, Model model) {
		return "";
	}
}
