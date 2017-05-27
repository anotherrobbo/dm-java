package com.anotherrobbo.dm.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.anotherrobbo.dm.freemarker.FreemarkerUtil;

@Path("/")
public class Index {
	
	@Inject
	FreemarkerUtil freemarkerUtil;
	
	@GET
	@Path("/")
	public Response index() {
		return freemarkerUtil.render("/index.ftl");
	}
}
