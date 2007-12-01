package org.net9.redbud.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.storage.hibernate.userinfo.UserinfoDAO;
import org.net9.redbud.web.util.JsonUtils;
import org.springframework.web.servlet.ModelAndView;

public class UserinfoController extends RedbudBaseController {

	private UserinfoDAO userinfoDAO;

	private LoginDAO loginDAO;

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	public void setUserinfoDAO(UserinfoDAO userinfoDAO) {
		this.userinfoDAO = userinfoDAO;
	}

	public ModelAndView viewUserinfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Userinfo info = (Userinfo) request.getSession()
				.getAttribute("userinfo");
		PrintWriter pw = response.getWriter();
		pw.print(JsonUtils.getJsonObjectForUserinfo(info));
		pw.close();
		return null;
	}

	public ModelAndView editUserinfo(HttpServletRequest request,
			HttpServletResponse response) {
		Userinfo info = (Userinfo) request.getSession()
				.getAttribute("userinfo");
		info.setTel(request.getParameter("tel"));
		info.setMobile(request.getParameter("mobile"));
		info.setEmail(request.getParameter("email"));
		info.setAddress(request.getParameter("address"));
		userinfoDAO.update(info);
		return null;
	}

	public ModelAndView changePassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Login login = ((Userinfo) request.getSession().getAttribute("userinfo"))
				.getLogin();
		PrintWriter pw = response.getWriter();
		if (!login.getPassword().equals(request.getParameter("oldPwd"))) {
			pw.print("密码错误");
		} else {
			login.setPassword(request.getParameter("newPwd"));
			loginDAO.update(login);
			pw.print("1");
		}
		pw.close();
		return null;
	}
}
