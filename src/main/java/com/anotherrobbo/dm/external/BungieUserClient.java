package com.anotherrobbo.dm.external;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.databind.JsonNode;

@Path("Platform")
public interface BungieUserClient {

	@GET
	@Path("/User/GetBungieAccount/{id}/{systemCode}/")
	public JsonNode playerSummary(@PathParam("systemCode") int systemCode, @PathParam("id") long id);
	
}
