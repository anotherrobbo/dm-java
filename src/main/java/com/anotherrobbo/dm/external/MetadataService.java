package com.anotherrobbo.dm.external;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.jboss.logging.Logger;

import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;

@Singleton
public class MetadataService {
	private Logger log = Logger.getLogger(getClass());
	private Map<String, String> metadataMap = new HashMap<String, String>();
	
	public String getDef(String type, String hash) {
	    return getDef(type, type, hash);
	}

	public String getDef(String type, String typeDef, String hash) {
		String key = type + "-" + hash;
		if (!metadataMap.containsKey(key)) {
			log.info("Loading " + key);
			try {
				String data = BungieInterface.getMetadata(type, typeDef, hash);
				metadataMap.put(key, data);
				log.info(data);
			} catch (BungieInterfaceException e) {
				e.printStackTrace();
			}
		}
		return metadataMap.get(key);
	}
	
}
