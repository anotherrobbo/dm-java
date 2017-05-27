package com.anotherrobbo.dm.rest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.anotherrobbo.dm.freemarker.FreemarkerUtil;
import com.anotherrobbo.dm.model.Player;
import com.anotherrobbo.dm.service.PlayerService;

@Path("/overview")
public class Overview {
	
	@Inject
	private FreemarkerUtil freemarkerUtil;
	
	@Inject
	private PlayerService playerService;

	@GET
	@Path("{system}/{name}")
	public Response getOverview(@PathParam("system") String system, @PathParam("name") String name) {
		Map<String, Object> dataModel = new HashMap<>();
		Player p = playerService.getPlayer(system, name);
		dataModel.put("player", p);
		return freemarkerUtil.render(dataModel, "/player/show.ftl");
	}
	
}
