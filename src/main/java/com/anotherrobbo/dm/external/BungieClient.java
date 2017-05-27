package com.anotherrobbo.dm.external;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;

public interface BungieClient {

	@GET
	@Path("/Platform/Destiny/SearchDestinyPlayer/All/{name}/")
	public JsonNode searchPlayers(@PathParam("name") String name);
	
}
