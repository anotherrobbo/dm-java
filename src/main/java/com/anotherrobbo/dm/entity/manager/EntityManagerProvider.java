package com.anotherrobbo.dm.entity.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

/**
 * Uses properties specifically for AWS / Heroku
 * 
 */
public class EntityManagerProvider {
	
	private static final String PROP_DB_USERNAME = "RDS_USERNAME";
	private static final String PROP_DB_PASSWORD = "RDS_PASSWORD";
	private static final String PROP_DB_HOSTNAME = "RDS_HOSTNAME";
	private static final String PROP_DB_PORT = "RDS_PORT";
	private static final String PROP_DB_NAME = "RDS_DB_NAME";
	private static final String PROP_DB_URL = "DATABASE_URL";
	
	private static final String PROP_CONN_USERNAME = "javax.persistence.jdbc.user";
	private static final String PROP_CONN_PASSWORD = "javax.persistence.jdbc.password";
	private static final String PROP_CONN_URL = "javax.persistence.jdbc.url";

	public static Map<String, String> getPropertyOverrides() {
		Map<String, String> overrides = new HashMap<String, String>();
		if (StringUtils.isNotBlank(getProperty(PROP_DB_USERNAME))) {
			overrides.put(PROP_CONN_USERNAME, getProperty(PROP_DB_USERNAME));
		}
		if (StringUtils.isNotBlank(getProperty(PROP_DB_PASSWORD))) {
			overrides.put(PROP_CONN_PASSWORD, getProperty(PROP_DB_PASSWORD));
		}
		if (StringUtils.isNotBlank(getProperty(PROP_DB_HOSTNAME)) &&
				StringUtils.isNotBlank(getProperty(PROP_DB_PORT)) &&
				StringUtils.isNotBlank(getProperty(PROP_DB_NAME))) {
			String url = "jdbc:postgresql://" + getProperty(PROP_DB_HOSTNAME) + ":" + getProperty(PROP_DB_PORT) + "/" + getProperty(PROP_DB_NAME);
			overrides.put(PROP_CONN_URL, url);
		} else if (StringUtils.isNotBlank(getProperty(PROP_DB_URL))) {
		    String url = "jdbc:" + getProperty(PROP_DB_URL);
            overrides.put(PROP_CONN_URL, url);
		}
		Logger.getLogger(EntityManagerProvider.class).info("Found " + overrides.size());
		return overrides;
	}
	
	private static String getProperty(String propName) {
	    return System.getProperty(propName, System.getenv(propName));
	}

}
