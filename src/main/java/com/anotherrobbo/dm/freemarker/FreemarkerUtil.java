package com.anotherrobbo.dm.freemarker;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtil {

	private Configuration config;
	private ServletContext servletContext;
	
	@Inject
	public FreemarkerUtil(ServletContext servletContext) {
		this.config = new Configuration(Configuration.VERSION_2_3_26);
		this.servletContext = servletContext;
		config.setServletContextForTemplateLoading(servletContext, "WEB-INF/freemarker");
	}
	
	public Response render(String template) {
		return render(new HashMap<String, Object>(),  template);
	}
	
	public Response render(Map<String, Object> dataModel, String template) {
		try {
			dataModel.put("url", new URLBean(servletContext));
			Template ftlTemplate = config.getTemplate(template);
			StringWriter writer = new StringWriter();
			ftlTemplate.process(dataModel, writer);
			return Response.ok(writer.toString()).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
}
