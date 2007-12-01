package org.net9.redbud.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.web.commandclass.LoginCommandClass;
import org.net9.redbud.web.util.MD5Util;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class LoginController extends SimpleFormController {

	private LoginDAO loginDAO;

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException errors)
			throws Exception {
		LoginCommandClass lcc = (LoginCommandClass) cmd;

		String username = lcc.getStudentnum();
		String password = lcc.getPassword();
		if (org.net9.redbud.web.util.ParseUsername.parse(username) == false) {
			
			//如果用户名不是root
			response.sendRedirect("http://51.test.thcic.cn/roam/AppRoam.do?appID=XSQH_SHEGONG&action=login&user="+username+"&pass="+password);
			return null;
		}

		List list = loginDAO.findByStudentnum(lcc.getStudentnum());
		Iterator it = list.iterator();
		// studentnum doesn't exist
		if (!it.hasNext()) {
			response.sendRedirect("login1.html");
		}
		// studentnum exists
		else {
			Login login = (Login) it.next();
			// password is correct
			// MD5Util md5;
			if (MD5Util.MD5(lcc.getPassword()).equals(login.getPassword())) {
				ArrayList<String> posts = new ArrayList<String>();
				Iterator iter = login.getPostses().iterator();

				while (iter.hasNext()) {
					String postNum = ((Posts) iter.next()).getFullcode();
					posts.add(postNum);
				}
				request.getSession().setAttribute("studentnum",
						lcc.getStudentnum());
				request.getSession().setAttribute("userinfo",
						login.getUserinfo());
				request.getSession().setAttribute("postList", posts);
				response.sendRedirect("index.jsp");
				// password is incorrect
			} else {
				response.sendRedirect("login.html?t0");
			}
		}
		return null;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

}
