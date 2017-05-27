package com.anotherrobbo.dm.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.model.Player;
import com.fasterxml.jackson.databind.JsonNode;

@Path("/search")
public class Search {
	
	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> search(@PathParam("name") String name) {
		List<Player> players = new ArrayList<>();
		
		try {
			JsonNode jsonPlayers = BungieInterface.searchPlayers(name);
			JsonNode pArray = jsonPlayers.get("Response");
			players.addAll(convertPlayers(pArray));
		} catch (BungieInterfaceException e) {
			e.printStackTrace();
		}
		
		return players;
	}

	private Collection<? extends Player> convertPlayers(JsonNode pArray) {
		List<Player> players = new ArrayList<>();
		Iterator<JsonNode> elements = pArray.elements();
		while(elements.hasNext()) {
			JsonNode next = elements.next();
			Player p = new Player();
			p.setSystemCode(next.get("membershipType").asInt());
			switch (p.getSystemCode()) {
				case 2:
					p.setSystem("ps");
					break;
				case 1:
					p.setSystem("xb");
					break;
			}
			p.setName(next.get("displayName").asText());
			p.setId(next.get("membershipId").asLong());
			p.setSystemIcon(BungieInterface.BUNGIE_URL + next.get("iconPath").asText());
			players.add(p);
		}
		return players;
	}
	
}
