package com.anotherrobbo.dm.job;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;
import org.sqlite.JDBC;

import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.MetadataService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManifestJob implements Runnable {
	
	private final Logger log = Logger.getLogger(getClass());
	private final MetadataService metadataService;
	
	public ManifestJob(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

	@Override
	public void run() {
	    log.info("Checking last manifest updated...");
	    Instant lastUpdate = metadataService.getLastUpdate();
	    
	    if (lastUpdate != null && lastUpdate.isBefore(Instant.now().minus(1, ChronoUnit.DAYS))) {
	        log.info("Last updated less than 1 day ago so skipping load - " + lastUpdate);
	        long secs = Duration.between(lastUpdate.plus(1, ChronoUnit.DAYS), Instant.now()).toMillis() / 1000;
	        log.info("Scheduling next refresh for " + secs + " seconds from now");
	        metadataService.refreshIn(secs);
	    } else {
	        log.info("Reloading manifest data");
	        try {
    	        JsonNode data = BungieInterface.getMetadataInfo();
    	        String dbPath = data.get("mobileWorldContentPaths").get("en").asText();
    	        log.info("downloading data: " + dbPath);
    	        File tempZip = File.createTempFile("manifest", ".zip");
    	        FileUtils.copyURLToFile(new URL(BungieInterface.BUNGIE_URL + dbPath), tempZip);
    
    	        log.info("Download complete, unzipping data");
    	        File tempDb = File.createTempFile("manifest", ".sqlite");
    	        ZipFile zip = new ZipFile(tempZip);
    	        FileUtils.copyToFile(new BufferedInputStream(zip.getInputStream(zip.getEntries().nextElement())), tempDb);
    	        zip.close();
    	        log.info("Unzipping complete, processing data");
    	
    	        Connection connection = null;
    	        try {
    	            // db parameters
    	            String url = "jdbc:sqlite:" + tempDb.getAbsolutePath();
    	            // create a connection to the database
    	            if (JDBC.isValidURL(url)) {
        	            connection = DriverManager.getConnection(url);
        	            log.info("Connection to SQLite has been established.");
        	            loadFromTable(connection, "DestinyClassDefinition", "class");
        	            loadFromTable(connection, "DestinyRaceDefinition", "race");
        	            loadFromTable(connection, "DestinyGenderDefinition", "gender");
        	            loadFromTable(connection, "DestinyActivityDefinition", "activity");
        	            loadFromTable(connection, "DestinyActivityTypeDefinition", "activityType");
    	            } else {
    	                log.error("Invalid sqlite db url: " + url);
    	            }
    	        } catch (SQLException e) {
    	            log.error("SQLite access problem.", e);
    	        } finally {
    	            try {
    	                if (connection != null) {
    	                    connection.close();
    	                }
    	            } catch (SQLException ex) {
    	                System.out.println(ex.getMessage());
    	            }
    	        }
	        } catch (Exception e) {
	            log.error("Something went wrong during manifest refresh", e);
	        }
	
            log.info("Manifest data update finished, scheduling next refresh for 1 day from now");
	        metadataService.setLastUpdate(Instant.now());
	        metadataService.refreshIn(24 * 60 * 60);
	    }
	}
	
	protected void loadFromTable(Connection connection, String tableName, String type) throws SQLException {
	    PreparedStatement call = connection.prepareStatement("select json from " + tableName);
	    ResultSet resultSet = call.executeQuery();
	    while (resultSet.next()) {
	        String jsonString = resultSet.getString(1);
	        try {
    	        ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
    	        String hash = node.get("hash").asText(); 
    	        metadataService.setDefData(type + "-" + hash, node);
	        } catch (IOException e) {
	            log.error("Unable to parse json in " + jsonString);
	        }
	    }
	}
	
}
