package com.anotherrobbo.dm.entity.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

/**
 * Uses properties specifically for AWS 
 * 
 */
public class EntityManagerProvider {
	
	private static final String PROP_DB_USERNAME = "RDS_USERNAME";
	private static final String PROP_DB_PASSWORD = "RDS_PASSWORD";
	private static final String PROP_DB_HOSTNAME = "RDS_HOSTNAME";
	private static final String PROP_DB_PORT = "RDS_PORT";
	private static final String PROP_DB_NAME = "RDS_DB_NAME";
	
	private static final String PROP_CONN_USERNAME = "javax.persistence.jdbc.user";
	private static final String PROP_CONN_PASSWORD = "javax.persistence.jdbc.password";
	private static final String PROP_CONN_URL = "javax.persistence.jdbc.url";

	public static Map<String, String> getPropertyOverrides() {
		Map<String, String> overrides = new HashMap<String, String>();
		if (StringUtils.isNotBlank(System.getProperty(PROP_DB_USERNAME))) {
			overrides.put(PROP_CONN_USERNAME, System.getProperty(PROP_DB_USERNAME));
		}
		if (StringUtils.isNotBlank(System.getProperty(PROP_DB_PASSWORD))) {
			overrides.put(PROP_CONN_PASSWORD, System.getProperty(PROP_DB_PASSWORD));
		}
		if (StringUtils.isNotBlank(System.getProperty(PROP_DB_HOSTNAME)) &&
				StringUtils.isNotBlank(System.getProperty(PROP_DB_PORT)) &&
				StringUtils.isNotBlank(System.getProperty(PROP_DB_NAME))) {
			String url = "jdbc:postgresql://" + System.getProperty(PROP_DB_HOSTNAME) + ":" + System.getProperty(PROP_DB_PORT) + "/" + System.getProperty(PROP_DB_NAME);
			overrides.put(PROP_CONN_URL, url);
		}
		Logger.getLogger(EntityManagerProvider.class).info("Found " + overrides.size() + " overrides");
		return overrides;
	}

}
