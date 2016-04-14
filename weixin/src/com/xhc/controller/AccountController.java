package com.xhc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.xhc.entity.PicModel;
import com.xhc.model.Employee;
import com.xhc.model.TmLibrary;
import com.xhc.service.AccountService;

@Controller
public class AccountController {
	private AccountService accountService;
	protected static Map<String, Employee> employees = new HashMap<String, Employee>();
	private static final Log logger = LogFactory
			.getLog(AccountController.class);

	@RequestMapping(value = "/welcome")
	public String welcome() {
		return "login";
	}

	@RequestMapping(value = "/login")
	@ResponseBody
	public Map<String, String> login(HttpServletRequest request,
			HttpSession session, Model model) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String identity = request.getParameter("identity");
		Employee employee = accountService.login(username, password, identity);
		Map<String, String> map = new HashMap<String, String>(4);
		if (employee != null) {
			session.setAttribute("emNo", employee.getEmNo());
			employees.put(employee.getEmNo(), employee);
			map.put("emNo", employee.getEmNo());
			if (identity.equals("admin")) {
				map.put("result", "KFManage");
			} else {
				map.put("result", "KFClient");
			}
			return map;
		}
		map.put("result", "login");
		return map;
	}

	@RequestMapping(value = "/KFClient")
	public String toKFClient(HttpServletRequest request, HttpSession session,
			Model model) {
		if (session.getAttribute("emNo") == null)
			return "login";
		String emNo = request.getParameter("emNo");
		Employee employee = employees.get(emNo);
		model.addAttribute("employee1", employee);
		return "KFClient";
	}

	@RequestMapping(value = "/KFManage")
	public String toKFManage(HttpServletRequest request, HttpSession session,
			Model model) {
		if (session.getAttribute("emNo") == null)
			return "login";
		model.addAttribute("employee", new Employee());
		String emNo = request.getParameter("emNo");
		Employee employee = employees.get(emNo);
		model.addAttribute("employee1", employee);
		TmLibrary tl = new TmLibrary();
		Employee employee1 = new Employee();
		tl.setEmployee(employee1);
		model.addAttribute("tmLibrary", tl);
		model.addAttribute("picModel", new PicModel());
		return "KFManage";
	}

	@RequestMapping(value = "/logout")
	@ResponseBody
	public String logout(HttpServletRequest request, HttpSession session) {
		String username = request.getParameter("username");
		if (accountService.logout(username)) {
			employees.remove(session.getAttribute("emNo"));
			session.invalidate();
			return "success";
		}
		return "fail";
	}

	@RequestMapping(value = "/delUser")
	@ResponseBody
	public String delUser(HttpServletRequest request) {
		String id = request.getParameter("userId");
		try {
			accountService.deleteUser(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/disableUser")
	@ResponseBody
	public String disableUser(HttpServletRequest request) {
		String id = request.getParameter("userId");
		try {
			accountService.disableUser(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/updateUser.do")
	@ResponseBody
	public String updateUser(HttpServletRequest request) {
		String str = request.getParameter("jsonStr");
		try {
			accountService.updateUser(str);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@RequestMapping(value = "/changePassword.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String changePassword(HttpServletRequest request) {
		String str = request.getParameter("jsonStr");
		String message = accountService.changePassword(str);
		return message;
	}

	@RequestMapping(value = "/enableUser")
	@ResponseBody
	public String enableUser(HttpServletRequest request) {
		String id = request.getParameter("userId");
		try {
			accountService.enableUser(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/userList", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getUserList(HttpServletRequest request) {
		int offset = request.getParameter("offset") == null ? 1 : Integer
				.parseInt(request.getParameter("offset"));
		// 每页行数
		int showCount = request.getParameter("limit") == null ? 10 : Integer
				.parseInt(request.getParameter("limit"));
		String search = request.getParameter("search") == null ? "" : request
				.getParameter("search");
		Map<String, Object> map = accountService.getUserList(offset / showCount
				+ 1, showCount, search);
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	@RequestMapping(value = "/addUser")
	public String addUser(@Valid @ModelAttribute("employee") Employee employee,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpSession session) {
		TmLibrary tl = new TmLibrary();
		Employee em = new Employee();
		tl.setEmployee(em);
		model.addAttribute("tmLibrary", tl);
		Employee employee1 = employees.get(session.getAttribute("emNo"));
		model.addAttribute("employee1", employee1);
		model.addAttribute("picModel", new PicModel());
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			logger.info("Code:" + fieldError.getCode() + ", object:"
					+ fieldError.getObjectName() + ", field:"
					+ fieldError.getField());
			model.addAttribute("employee", employee);
			request.setAttribute("flag", "AddUserFail");
			return "KFManage";
		}
		accountService.addUser(employee);
		request.setAttribute("flag", "AddUserSuccess");
		return "KFManage";
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
}
