package com.xhc.controller;

import java.io.IOException;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 595182898846806975L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Scanner in = new Scanner(System.in);
		String str = in.next();
		req.setAttribute("name", str);
		req.getRequestDispatcher("index.jsp").forward(req, resp);
		in.close();
	}
}
