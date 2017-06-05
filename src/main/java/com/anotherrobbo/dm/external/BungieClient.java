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
	@Path("/Destiny/Stats/Account/{systemCode}/{id}/")
	public JsonNode playerChars(@PathParam("systemCode") int systemCode, @PathParam("id") long id);

	@GET
	@Path("/Destiny/Manifest/{type}/{hash}/")
	public JsonNode metadata(@PathParam("type") String type, @PathParam("hash") String hash);

	@GET
	@Path("/Destiny/Stats/ActivityHistory/{systemCode}/{pid}/{cid}/?definitions=false&mode=None&page=#{page}&count=#{count}")
	public JsonNode getCharActivities(@PathParam("systemCode") int systemCode, @PathParam("pid") long pid, @PathParam("cid") long cid, @PathParam("page") int page, @PathParam("count") int count);
	
}
