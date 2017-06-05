package com.anotherrobbo.dm.external;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.databind.JsonNode;

public class BungieInterface {
	public static final String BUNGIE_URL = "https://www.bungie.net";
	private static final String BUNGIE_API_KEY = System.getenv("BUNGIE_API_KEY");
	
	private static final Logger log = Logger.getLogger(BungieInterface.class);
	private static ThreadLocal<BungieClient> bungieClient = new ThreadLocal<BungieClient>() {
		protected BungieClient initialValue() {
			return createClient();
		};
	};
	
	public static JsonNode searchPlayers(String name) throws BungieInterfaceException {
		JsonNode players = bungieClient.get().searchPlayers(name);
		verifyResponse(players);
		return players.get("Response");
	}
	
	public static JsonNode getPlayerInfo(Integer systemCode, String name) throws BungieInterfaceException {
		JsonNode players = bungieClient.get().playerInfo(systemCode, name);
		verifyResponse(players);
		return players.get("Response").get(0);
	}
	
	public static JsonNode getPlayerSummary(int systemCode, long id) throws BungieInterfaceException {
		JsonNode players = bungieClient.get().playerSummary(systemCode, id);
		verifyResponse(players);
		return players.get("Response");
	}
	
	public static JsonNode getPlayerChars(int systemCode, long id) throws BungieInterfaceException {
		JsonNode players = bungieClient.get().playerChars(systemCode, id);
		verifyResponse(players);
		return players.get("Response").get("characters");
	}
	
	public static JsonNode getCharActivities(int systemCode, long pid, long cid, int page, int count) throws BungieInterfaceException {
		JsonNode activities = bungieClient.get().getCharActivities(systemCode, pid, cid, page, count);
		verifyResponse(activities);
		return activities.get("Response").get("data").get("activities");
	}
	
	public static String getMetadata(String type, String typeDef, String hash) throws BungieInterfaceException {
		JsonNode metadata = bungieClient.get().metadata(type, hash);
		verifyResponse(metadata);
		return metadata.get("Response").get(typeDef).asText();
	}

	private static void verifyResponse(JsonNode response) throws BungieInterfaceException {
		JsonNode jsonNode = response.get("ErrorCode");
		if (jsonNode.asInt(1) != 1) {
			throw new BungieInterfaceException(response.get("ErrorCode").asInt(), response.get("ErrorStatus").asText(), response.get("Message").asText());
		}
	}

	private static BungieClient createClient() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.register(new AuthHeaderFilter());

        BungieClient bungieClient = client.target(BUNGIE_URL).proxy(BungieClient.class);
        log.info("new Bungie Client created");
        return bungieClient;
	}
	
	private static class AuthHeaderFilter implements ClientRequestFilter {
		@Override
		public void filter(ClientRequestContext context) throws IOException {
			context.getHeaders().add("X-API-Key", BUNGIE_API_KEY);
		}
	}
	
	public static class BungieInterfaceException extends Exception {
		public BungieInterfaceException(int errorCode, String errorStatus, String errorMessage) {
			super(errorCode + " : " + errorMessage);
		}
	}

}
