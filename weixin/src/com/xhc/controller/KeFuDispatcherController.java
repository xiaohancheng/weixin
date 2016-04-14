package com.xhc.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xhc.service.KeFuDispatcherService;

@Controller
public class KeFuDispatcherController {
	private KeFuDispatcherService keFuDispatcherService;

	@RequestMapping(value = "/jieru.do")
	public void jieruKeFu(@RequestParam String username,
			@RequestParam String kefuAccount, HttpServletResponse resp) {
		String str;
		if (keFuDispatcherService.assignKefu(username, kefuAccount))
			str = "success";
		else
			str = "fail";
		try {
			resp.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/get_picture.do")
	@ResponseBody
	public String get_picture() {
		return keFuDispatcherService.getPictures();
	}

	@Autowired
	public void setKeFuDispatcherService(
			KeFuDispatcherService keFuDispatcherService) {
		this.keFuDispatcherService = keFuDispatcherService;
	}

	@RequestMapping(value = "/onlinekefu.do")
	@ResponseBody
	public String getOnlineKeFu(HttpServletRequest request) {
		String account = request.getParameter("kefuAccount");
		return JSONArray.fromObject(
				keFuDispatcherService.getOnlineKeFu(account)).toString();
	}

	@RequestMapping(value = "/zhuanjie.do")
	@ResponseBody
	public String zhuanjie(HttpServletRequest request) {
		String str = request.getParameter("jsonStr");
		try {
			keFuDispatcherService.zhuanjie(str);
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/handlezhuanjie.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String handlezhuanjie(HttpServletRequest request) {
		String str = request.getParameter("jsonStr");
		try {
			return keFuDispatcherService.handlezhuanjie(str);
		} catch (Exception e) {
			return "fail";
		}
	}

	@RequestMapping(value = "/cancelhandlezhuanjie.do")
	@ResponseBody
	public String cancelhandlezhuanjie(HttpServletRequest request) {
		String str = request.getParameter("jsonStr");
		try {
			keFuDispatcherService.cancelhandlezhuanjie(str);
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}
}
