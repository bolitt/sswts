package org.net9.redbud.web.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.storage.hibernate.userinfo.UserinfoDAO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import thauth.ThauthConst;

public class NewLoginController extends AbstractController {
	private LoginDAO loginDAO;

	private UserinfoDAO userinfoDAO;

	public void setUserinfoDAO(UserinfoDAO userinfoDAO) {
		this.userinfoDAO = userinfoDAO;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userIp = request.getRemoteAddr();
		String ticket = request.getParameter("ticket");// 通过Request传过来的票据
		String appName = "XSQH_SHEGONG";// 这里是双方商定好的应用名字
		Hashtable ret = thauth.Roam.thauthCheckTicket(ticket, appName, userIp);// 检查票据是否有效
		int code = ((Integer) (ret.get(thauth.ThauthConst.THAUTH_CODE)))
				.intValue();
		if (code != thauth.ThauthConst.AUTHCODE_SUCCESS) {// 如果票据无效,抛出异常
			response.sendRedirect("login.html?t0");
		}
		// 下面取得用户的证件号和姓名
		String userId = (String) ret.get(ThauthConst.THAUTH_ZJH);
		List list = loginDAO.findByStudentnum(userId);
		Iterator it = list.iterator();
		Login login;
		if (!it.hasNext()) {
			login = new Login();
			login.setStudentnum(userId);
			loginDAO.save(login);
			Userinfo userinfo = new Userinfo();
			userinfo.setName((String) ret.get(ThauthConst.THAUTH_XM));
			userinfo.setDepartment(Short.parseShort((String) ret
					.get(ThauthConst.THAUTH_YHZT)));
			userinfoDAO.save(userinfo);
			login = (Login) loginDAO.findByStudentnum(userId).iterator().next();
		} else {
			login = (Login) it.next();
		}
		ArrayList<String> posts = new ArrayList<String>();
		Iterator iter = login.getPostses().iterator();

		while (iter.hasNext()) {
			String postNum = ((Posts) iter.next()).getFullcode();
			posts.add(postNum);
		}
		request.getSession().setAttribute("studentnum", userId);
		request.getSession().setAttribute("userinfo", login.getUserinfo());
		request.getSession().setAttribute("postList", posts);
		response.sendRedirect("index.jsp");
		return null;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

}
