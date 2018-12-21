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
	private static final String BUNGIE_API_KEY = System.getProperty("BUNGIE_API_KEY", System.getenv("BUNGIE_API_KEY"));
	
	private static final Logger log = Logger.getLogger(BungieInterface.class);
	private static ThreadLocal<BungieD1Client> bungieD1Client = new ThreadLocal<BungieD1Client>() {
		protected BungieD1Client initialValue() {
			return createD1Client();
		};
	};
	private static ThreadLocal<BungieUserClient> bungieUserClient = new ThreadLocal<BungieUserClient>() {
        protected BungieUserClient initialValue() {
            return createUserClient();
        };
    };
	
	public static JsonNode searchPlayers(String name) throws BungieInterfaceException {
		JsonNode players = bungieD1Client.get().searchPlayers(name);
		verifyResponse(players);
		return players.get("Response");
	}
	
	public static JsonNode getPlayerInfo(Integer systemCode, String name) throws BungieInterfaceException {
		JsonNode players = bungieD1Client.get().playerInfo(systemCode, name);
		verifyResponse(players);
		return players.get("Response").get(0);
	}
	
	public static JsonNode getPlayerSummary(int systemCode, long id) throws BungieInterfaceException {
		JsonNode players = bungieD1Client.get().playerSummary(systemCode, id);
		verifyResponse(players);
		return players.get("Response");
	}
	
	public static JsonNode getPlayerChars(int systemCode, long id) throws BungieInterfaceException {
		JsonNode players = bungieD1Client.get().playerChars(systemCode, id);
		verifyResponse(players);
		return players.get("Response").get("characters");
	}
	
	public static JsonNode getCharActivities(int systemCode, long pid, long cid, int page, int count) throws BungieInterfaceException {
		JsonNode activities = bungieD1Client.get().getCharActivities(systemCode, pid, cid, page, count, false, "None");
		verifyResponse(activities);
		return activities.get("Response").get("data").get("activities");
	}
	
	public static JsonNode getActivityDetails(long id) throws BungieInterfaceException {
		JsonNode activityDetails = bungieD1Client.get().getActivityDetails(id);
		verifyResponse(activityDetails);
		return activityDetails.get("Response").get("data");
	}
	
	public static JsonNode getMetadataInfo() throws BungieInterfaceException {
		JsonNode metadata = bungieD1Client.get().getMetadataInfo();
		verifyResponse(metadata);
		return metadata.get("Response");
	}
	
	public static JsonNode getMetadata(String type, String hash) throws BungieInterfaceException {
		JsonNode metadata = bungieD1Client.get().getMetadata(type, hash);
		verifyResponse(metadata);
		return metadata.get("Response").get("data").get(type);
	}

	private static void verifyResponse(JsonNode response) throws BungieInterfaceException {
		JsonNode jsonNode = response.get("ErrorCode");
		if (jsonNode.asInt(1) != 1) {
			throw new BungieInterfaceException(response.get("ErrorCode").asInt(), response.get("ErrorStatus").asText(), response.get("Message").asText());
		}
	}

	private static BungieD1Client createD1Client() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.register(new AuthHeaderFilter());

        BungieD1Client bungieClient = client.target(BUNGIE_URL).proxy(BungieD1Client.class);
        log.info("new Bungie D1 Client created");
        return bungieClient;
	}
	
	private static BungieUserClient createUserClient() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        client.register(new AuthHeaderFilter());

        BungieUserClient bungieClient = client.target(BUNGIE_URL).proxy(BungieUserClient.class);
        log.info("new Bungie User Client created");
        return bungieClient;
    }
	
	private static class AuthHeaderFilter implements ClientRequestFilter {
		@Override
		public void filter(ClientRequestContext context) throws IOException {
			context.getHeaders().add("X-API-Key", BUNGIE_API_KEY);
		}
	}
	
	public static class BungieInterfaceException extends Exception {
		private static final long serialVersionUID = 1L;

		public BungieInterfaceException(int errorCode, String errorStatus, String errorMessage) {
			super(errorCode + " : " + errorMessage);
		}
	}

}
