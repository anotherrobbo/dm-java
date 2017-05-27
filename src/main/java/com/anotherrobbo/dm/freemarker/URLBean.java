package com.anotherrobbo.dm.freemarker;

import javax.servlet.ServletContext;

public class URLBean {
	
	private ServletContext servletContext;
	
	public URLBean(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getResourcePath() {
		return servletContext.getContextPath() + "/resources/";
	}
	
}
