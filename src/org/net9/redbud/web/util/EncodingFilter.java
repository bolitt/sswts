package org.net9.redbud.web.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncodingFilter implements Filter{
	protected FilterConfig filterConfig;
	
	public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest srequest, ServletResponse sresponse,
            FilterChain filterChain) {
    	try{
    		//System.out.println("in filer");
    		HttpServletResponse response=(HttpServletResponse)sresponse;
    		response.setCharacterEncoding("UTF-8");
    		HttpServletRequest request=(HttpServletRequest)srequest;
    		request.setCharacterEncoding("UTF-8");
    		filterChain.doFilter(srequest, sresponse);
    	}catch (ServletException sx){
    		filterConfig.getServletContext().log(sx.getMessage());
    	}catch (IOException iox){
    		filterConfig.getServletContext().log(iox.getMessage());
    	}
    }
    
    public void destroy() {
    	this.filterConfig=null;
    }
}
    