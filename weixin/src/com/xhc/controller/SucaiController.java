package com.xhc.controller;

import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.xhc.entity.PicModel;
import com.xhc.model.Employee;
import com.xhc.model.TmLibrary;
import com.xhc.service.SucaiService;

@Controller
public class SucaiController {
	private SucaiService sucaiService;
	private static final Log logger = LogFactory
			.getLog(AccountController.class);

	@Autowired
	public void setSucaiService(SucaiService sucaiService) {
		this.sucaiService = sucaiService;
	}

	@RequestMapping(value = "/addTextMessage")
	public String addTextMessage(
			@Valid @ModelAttribute("tmLibrary") TmLibrary tmLibrary,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpSession session) {
		model.addAttribute("employee", new Employee());
		Employee employee1 = AccountController.employees.get(session
				.getAttribute("emNo"));
		model.addAttribute("employee1", employee1);
		model.addAttribute("picModel", new PicModel());
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			logger.info("Code:" + fieldError.getCode() + ", object:"
					+ fieldError.getObjectName() + ", field:"
					+ fieldError.getField());
			model.addAttribute("tmLibrary", tmLibrary);
			request.setAttribute("flag", "AddTextMsgFail");
			return "KFManage";
		}
		sucaiService.addTextMsg(tmLibrary);
		request.setAttribute("flag", "AddTextMsgSuccess");
		return "KFManage";
	}

	@RequestMapping(value = "/editTextMessage")
	public String editTextMessage(
			@Valid @ModelAttribute("tmLibrary") TmLibrary tmLibrary,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpSession session) {
		model.addAttribute("employee", new Employee());
		Employee employee1 = AccountController.employees.get(session
				.getAttribute("emNo"));
		model.addAttribute("employee1", employee1);
		model.addAttribute("picModel", new PicModel());
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			logger.info("Code:" + fieldError.getCode() + ", object:"
					+ fieldError.getObjectName() + ", field:"
					+ fieldError.getField());
			model.addAttribute("tmLibrary", tmLibrary);
			request.setAttribute("flag", "editTextMsgFail");
			return "KFManage";
		}
		sucaiService.editTextMsg(tmLibrary);
		request.setAttribute("flag", "editTextMsgSuccess");
		return "KFManage";
	}

	@RequestMapping(value = "/textMsgList", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getTextMsgList(HttpServletRequest request) {
		int offset = request.getParameter("offset") == null ? 1 : Integer
				.parseInt(request.getParameter("offset"));
		// 每页行数
		int showCount = request.getParameter("limit") == null ? 10 : Integer
				.parseInt(request.getParameter("limit"));
		String search = request.getParameter("search") == null ? "" : request
				.getParameter("search");
		Map<String, Object> map = sucaiService.getTextMsgList(offset
				/ showCount + 1, showCount, search);
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	@RequestMapping(value = "/textMsgListNotFenYe", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getTextMsgListNotFenYe(HttpServletRequest request) {
		String search = request.getParameter("search") == null ? "" : request
				.getParameter("search");
		Map<String, Object> map = sucaiService.getTextMsgList(search);
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	@RequestMapping(value = "/delTextMsg")
	@ResponseBody
	public String delTextMsg(HttpServletRequest request) {
		String id = request.getParameter("tlId");
		try {
			sucaiService.deleteTextMsg(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/disableTextMsg")
	@ResponseBody
	public String disableTextMsg(HttpServletRequest request) {
		String id = request.getParameter("tlId");
		try {
			sucaiService.disableTextMsg(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/enableTextMsg")
	@ResponseBody
	public String enableTextMsg(HttpServletRequest request) {
		String id = request.getParameter("tlId");
		try {
			sucaiService.enableTextMsg(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/addPicMessage")
	public String addPicMessage(HttpServletRequest servletRequest,
			@Valid @ModelAttribute("picModel") PicModel picModel,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpSession session) {
		model.addAttribute("employee", new Employee());
		TmLibrary tl = new TmLibrary();
		Employee employee = new Employee();
		tl.setEmployee(employee);
		model.addAttribute("tmLibrary", tl);
		Employee employee1 = AccountController.employees.get(session
				.getAttribute("emNo"));
		model.addAttribute("employee1", employee1);
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			System.out
					.println(fieldError.getCode() + "//"
							+ fieldError.getObjectName() + "//"
							+ fieldError.getField());
			logger.info("Code:" + fieldError.getCode() + ", object:"
					+ fieldError.getObjectName() + ", field:"
					+ fieldError.getField());
			model.addAttribute("picModel", picModel);
			request.setAttribute("flag", "AddPicMsgFail");
			return "KFManage";
		}
		List<MultipartFile> files = picModel.getImages();
		if (null != files && files.size() > 0) {
			sucaiService.addPicMsg(picModel,
					(String) session.getAttribute("emNo"));
		}
		request.setAttribute("flag", "AddPicMsgSuccess");
		// model.addAttribute("picModel", picModel);
		return "KFManage";
	}

	@RequestMapping(value = "/picMsgList", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getPicMsgList(HttpServletRequest request) {
		int offset = request.getParameter("offset") == null ? 1 : Integer
				.parseInt(request.getParameter("offset"));
		// 每页行数
		int showCount = request.getParameter("limit") == null ? 4 : Integer
				.parseInt(request.getParameter("limit"));
		String search = request.getParameter("search") == null ? "" : request
				.getParameter("search");
		Map<String, Object> map = sucaiService.getpicMsgList(offset / showCount
				+ 1, showCount, search);
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	@RequestMapping(value = "/delPicMsg")
	@ResponseBody
	public String delPicMsg(HttpServletRequest request) {
		String id = request.getParameter("plId");
		try {
			sucaiService.deletePicMsg(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/disablePicMsg")
	@ResponseBody
	public String disablePicMsg(HttpServletRequest request) {
		String id = request.getParameter("plId");
		try {
			sucaiService.disablePicMsg(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/enablePicMsg")
	@ResponseBody
	public String enablePicMsg(HttpServletRequest request) {
		String id = request.getParameter("plId");
		try {
			sucaiService.enablePicMsg(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/editPicMessage")
	public String editPicMessage(HttpServletRequest servletRequest,
			@Valid @ModelAttribute("picModel") PicModel picModel,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpSession session) {
		model.addAttribute("employee", new Employee());
		Employee employee1 = AccountController.employees.get(session
				.getAttribute("emNo"));
		model.addAttribute("employee1", employee1);
		TmLibrary tl = new TmLibrary();
		Employee employee = new Employee();
		tl.setEmployee(employee);
		model.addAttribute("tmLibrary", tl);
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			System.out
					.println(fieldError.getCode() + "//"
							+ fieldError.getObjectName() + "//"
							+ fieldError.getField());
			logger.info("Code:" + fieldError.getCode() + ", object:"
					+ fieldError.getObjectName() + ", field:"
					+ fieldError.getField());
			model.addAttribute("picModel", picModel);
			request.setAttribute("flag", "editPicMsgFail");
			return "KFManage";
		}
		sucaiService
				.editPicMsg(picModel, (String) session.getAttribute("emNo"));
		request.setAttribute("flag", "editPicMsgSuccess");
		return "KFManage";
	}
}
