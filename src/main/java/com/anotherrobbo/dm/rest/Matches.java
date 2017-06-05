package com.anotherrobbo.dm.rest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.freemarker.FreemarkerUtil;
import com.anotherrobbo.dm.job.MatchProcess;
import com.anotherrobbo.dm.service.MatchService;

@Path("/match")
public class Matches {
	
	@Inject
	private FreemarkerUtil freemarkerUtil;
	
	@Inject
	private MatchService matchService;

	@GET
	@Path("{system}/{name}/{name2}")
	public Response getMatches(@PathParam("system") String system, @PathParam("name") String name, @PathParam("name2") String name2) throws BungieInterfaceException {
		Map<String, Object> dataModel = new HashMap<>();
		MatchProcess p = matchService.findMatches(system, name, name2);
		dataModel.put("proc", p);
		return freemarkerUtil.render(dataModel, "/player/matches.ftl");
	}
	
	@GET
	@Path("poll/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MatchProcess pollProcess(@PathParam("id") String id) {
		return matchService.pollProcess(id);
	}
	
}
