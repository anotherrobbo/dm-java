package com.anotherrobbo.dm.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.external.MetadataService;
import com.anotherrobbo.dm.model.ActivityStats;
import com.anotherrobbo.dm.model.PlayerStats;
import com.anotherrobbo.dm.model.TeamStats;
import com.fasterxml.jackson.databind.JsonNode;

public class ActivityService {
    
    @Inject
    private MetadataService metadataService;

	public ActivityStats getActivityStats(long id) throws BungieInterfaceException {
	    ActivityStats a = new ActivityStats();
	    JsonNode act = BungieInterface.getActivityReport(id);
	    // Not all have teams
	    if (act.hasNonNull("teams") && act.get("teams").size() > 0) {
	        a.setTeamStats(getTeamStats(act));
	    } else {
	        a.setPlayerStats(getPlayerStats(act, null));
	    }
	    a.setId(id);
	    a.setPeriod(Timestamp.from(Instant.parse(act.get("period").textValue())));
	    boolean useType = act.get("activityDetails").get("activityTypeHashOverride").asInt() > 0 && act.get("activityDetails").get("mode").asInt() != 4;
	    a.setActivityTypeHash(useType ? act.get("activityDetails").get("activityTypeHashOverride").asText() : null);
	    a.setActivityHash(act.get("activityDetails").get("referenceId").asText());
	    a.setActivityName(metadataService.getDef("activity", "activityName", a.getActivityHash()));
	    a.setDuration(getDuration(act));
	    String iconUrl = null;
	    if (StringUtils.isNotBlank(a.getActivityTypeHash())) {
	        a.setActivityType(metadataService.getDef("activityType", "activityTypeName", a.getActivityTypeHash()));
	        iconUrl = metadataService.getDef("activityType", "icon", a.getActivityTypeHash());
	    } else { 
	        iconUrl = metadataService.getDef("activity", "icon", a.getActivityHash());
	    }
	    // iconUrl can be nil if activity is classified
	    if (StringUtils.isNotBlank(iconUrl)) {
	        a.setActivityIcon(BungieInterface.BUNGIE_URL + iconUrl);
	    }
	    return a;
	}
	
	private List<TeamStats> getTeamStats(JsonNode act) {
	    List<TeamStats> teams = new ArrayList<>();
	    Iterator<JsonNode> iterator = act.get("teams").iterator();
	    while (iterator.hasNext()) {
	        JsonNode teamEntry = iterator.next();
	        TeamStats t = new TeamStats();
	        //@@log.info(act)
	        t.setId(teamEntry.get("teamId").asLong());
	        t.setName(teamEntry.get("teamName").asText());
	        t.setResult(teamEntry.get("standing").get("basic").get("displayValue").asText());
	        t.setScore(teamEntry.get("score").get("basic").get("value").asInt());
	        t.setPlayerStats(getPlayerStats(act, t.getId()));
	        teams.add(t);
	    }
	    List<PlayerStats> noTeam = getPlayerStats(act, -1L);
	    if (!noTeam.isEmpty()) {
	        TeamStats t = new TeamStats();
            //@@log.info(act)
            t.setId(-1L);
            t.setName("No Team");
            t.setResult("");
            t.setScore(0);
            t.setPlayerStats(noTeam);
            teams.add(t);
	    }
	    return teams;
	}
	
	private List<PlayerStats> getPlayerStats(JsonNode act, Long teamId) {
	    //@@log.info("Get player stats #{teamId}")
	    List<PlayerStats> players = new ArrayList<>();
	    Iterator<JsonNode> iterator = act.get("entries").iterator();
        while (iterator.hasNext()) {
            JsonNode playerEntry = iterator.next();
	        if (teamId == null || teamId.equals(playerEntry.get("values").get("team").get("basic").get("value").asLong())) {
	            PlayerStats p = new PlayerStats();
	            //@@log.info(act)
	            p.setId(playerEntry.get("player").get("destinyUserInfo").get("membershipId").asLong());
	            p.setName(playerEntry.get("player").get("destinyUserInfo").get("displayName").asText());
	            p.setPlayerIcon(BungieInterface.BUNGIE_URL + playerEntry.get("player").get("destinyUserInfo").get("iconPath").asText());
	            p.setClassType(playerEntry.get("player").get("characterClass").asText());
	            p.setLevel(playerEntry.get("player").get("characterLevel").asInt());
	            p.setLight(playerEntry.get("player").get("lightLevel").asInt());
	            p.setScore(playerEntry.get("score").get("basic").get("value").asInt());
	            p.setK(playerEntry.get("values").get("kills").get("basic").get("value").asInt());
	            p.setA(playerEntry.get("values").get("assists").get("basic").get("value").asInt());
	            p.setD(playerEntry.get("values").get("deaths").get("basic").get("value").asInt());
	            p.setKd(playerEntry.get("values").get("killsDeathsRatio").get("basic").get("value").decimalValue());
	            p.setCompleted(playerEntry.get("values").get("completed").get("basic").get("value").asBoolean());
	            players.add(p);
	        }
        }
	    // Sort by score then kills
	    players.stream().sorted(Comparator.comparing(PlayerStats::getScore).thenComparing(PlayerStats::getK));
	    return players;
	}
	
	private String getDuration(JsonNode act) {
	    JsonNode playerEntry = act.get("entries").get(0);
	    String duration = playerEntry.get("extended").get("values").get("totalActivityDurationSeconds").get("basic").get("displayValue").asText();
	    return duration;
    }
	
}
