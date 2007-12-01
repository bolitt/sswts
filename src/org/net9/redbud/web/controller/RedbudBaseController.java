package org.net9.redbud.web.controller;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class RedbudBaseController extends MultiActionController {
	protected enum Browser {
		IE, IE6, IE7, FIREFOX, OTHER
	}

	protected void initBinder(ServletRequest request,
			ServletRequestDataBinder binder) {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(Short.class, null, new CustomNumberEditor(
				Short.class, true));
		binder.registerCustomEditor(Integer.class, null,
				new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Double.class, null, new CustomNumberEditor(
				Double.class, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(
				Long.class, true));
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("studentnum") == null) {
			// response.sendRedirect("login.html");
			PrintWriter out = response.getWriter();
			out.write("invalid");
			out.flush();
			out.close();
			return null;
		}
		return super.handleRequestInternal(request, response);
	}

	protected Browser getBrowserInfo(HttpServletRequest request) {
		String type = request.getHeader("User-Agent");
		if (type.contains("MSIE")) {
			if (type.contains("7.0")) {
				return Browser.IE7;
			} else if (type.contains("6.0")) {
				return Browser.IE6;
			}
			return Browser.IE;
		} else {
			if (type.contains("Firefox")) {
				return Browser.FIREFOX;
			}
		}
		return Browser.OTHER;
	}
}
