package com.anotherrobbo.dm.external;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.jboss.logging.Logger;

import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.job.ManifestJob;
import com.fasterxml.jackson.databind.JsonNode;

@Singleton
public class MetadataService {
	private Logger log = Logger.getLogger(getClass());
	private Map<String, JsonNode> metadataMap = new HashMap<>();
	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private Instant lastUpdate;
	
	public MetadataService() {
	    refreshIn(0);
    }
	
	public Instant getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(Instant lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public String getDef(String type, String hash) {
	    return getDef(type, type, hash);
	}

	public String getDef(String type, String typeDef, String hash) {
		String key = type + "-" + hash;
		if (!metadataMap.containsKey(key)) {
			log.info("Loading " + key);
			try {
				JsonNode data = BungieInterface.getMetadata(type, hash);
				metadataMap.put(key, data);
				log.info(data);
			} catch (BungieInterfaceException e) {
				e.printStackTrace();
			}
		}
		return metadataMap.get(key).get(typeDef).asText();
	}
	
	public void setDefData(String key, JsonNode data) {
	    metadataMap.put(key, data);
	}
	
	public void refreshIn(long secs) {
		executor.schedule(new ManifestJob(this), secs, TimeUnit.SECONDS);
	}
	
}
