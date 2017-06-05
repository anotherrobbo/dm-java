package com.anotherrobbo.dm.external;

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
		JsonNode response = BungieInterface.getPlayerChars(2, 4611686018450420334L);
		Assert.assertNotNull(response);
		log.info(response);
	}
	
}
