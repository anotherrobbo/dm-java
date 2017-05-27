package com.anotherrobbo.dm.external;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.databind.JsonNode;

@Path("/Platform")
public interface BungieClient {

	@GET
	@Path("/Destiny/SearchDestinyPlayer/All/{name}/")
	public JsonNode searchPlayers(@PathParam("name") String name);

	@GET
	@Path("/Destiny/SearchDestinyPlayer/{systemCode}/{name}/")
	public JsonNode playerInfo(@PathParam("systemCode") int systemCode, @PathParam("name") String name);

	@GET
	@Path("/User/GetBungieAccount/{id}/{systemCode}/")
	public JsonNode playerSummary(@PathParam("systemCode") int systemCode, @PathParam("id") long id);

	@GET
	@Path("/Destiny/Manifest/{type}/{hash}/")
	public JsonNode metadata(@PathParam("type") String type, @PathParam("hash") String hash);
	
}
