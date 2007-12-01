package org.net9.redbud.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.web.servlet.DispatcherServlet;

public class RedBudDispatcherServlet extends DispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8590723445482830678L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		String servletHome = config.getServletContext().getRealPath("WEB-INF");
		System.setProperty("redbud.servlet.home", servletHome);
		super.init(config);
	}

}
