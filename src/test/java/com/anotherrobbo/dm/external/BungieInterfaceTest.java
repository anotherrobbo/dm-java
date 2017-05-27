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
	
}
