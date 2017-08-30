package com.anotherrobbo.dm.external;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.fasterxml.jackson.databind.JsonNode;

@Path("d1/Platform")
public interface BungieD1Client {

	@GET
	@Path("/Destiny/SearchDestinyPlayer/All/{name}/")
	public JsonNode searchPlayers(@PathParam("name") String name);

	@GET
	@Path("/Destiny/SearchDestinyPlayer/{systemCode}/{name}/")
	public JsonNode playerInfo(@PathParam("systemCode") int systemCode, @PathParam("name") String name);
	
	@GET
    @Path("/Destiny/{systemCode}/Account/{id}/Summary/")
    public JsonNode playerSummary(@PathParam("systemCode") int systemCode, @PathParam("id") long id);

	@GET
	@Path("/Destiny/Stats/Account/{systemCode}/{id}/")
	public JsonNode playerChars(@PathParam("systemCode") int systemCode, @PathParam("id") long id);
	
	@GET
	@Path("/Destiny/Manifest/")
	public JsonNode getMetadataInfo();

	@GET
	@Path("/Destiny/Manifest/{type}/{hash}/")
	public JsonNode getMetadata(@PathParam("type") String type, @PathParam("hash") String hash);

	@GET
	@Path("/Destiny/Stats/ActivityHistory/{systemCode}/{pid}/{cid}/")
	public JsonNode getCharActivities(@PathParam("systemCode") int systemCode, @PathParam("pid") long pid, @PathParam("cid") long cid, @QueryParam("page") int page, @QueryParam("count") int count, @QueryParam("definitions") boolean defs, @QueryParam("mode") String mode);

	@GET
	@Path("/Destiny/Stats/PostGameCarnageReport/{id}/")
	public JsonNode getActivityDetails(@PathParam("id") long id);
	
}
