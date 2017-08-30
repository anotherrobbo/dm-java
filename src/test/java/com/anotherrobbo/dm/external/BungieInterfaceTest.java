package com.anotherrobbo.dm.external;

import java.time.ZonedDateTime;

import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class BungieInterfaceTest {
	
	private Logger log = Logger.getLogger(getClass());

	@Test
	public void testSearchPlayers() throws Exception {
		JsonNode response = BungieInterface.searchPlayers("eraseme");
		Assert.assertNotNull(response);
		log.info(response);
	}
	
	@Test
	public void testGetPlayerInfo() throws Exception {
		JsonNode response = BungieInterface.getPlayerInfo(2, "eraseme");
		Assert.assertNotNull(response);
		log.info(response);
	}
	
	@Test
	public void testGetPlayerSummary() throws Exception {
		JsonNode response = BungieInterface.getPlayerSummary(2, 4611686018450420334L);
		Assert.assertNotNull(response);
		log.info(response);
	}
	
	@Test
	public void testGetPlayerChars() throws Exception {
//		JsonNode response = BungieInterface.getPlayerChars(2, 4611686018450420334L);
//		Assert.assertNotNull(response);
//		log.info(response);
		log.info(ZonedDateTime.parse("2015-09-10T09:44:23Z"));
	}
	
	@Test
	public void testGetCharActivities() throws Exception {
		JsonNode response = BungieInterface.getCharActivities(2, 4611686018450420334L, 2305843009411304571L, 0, 10);
		Assert.assertNotNull(response);
		log.info(response);
	}
	
}
