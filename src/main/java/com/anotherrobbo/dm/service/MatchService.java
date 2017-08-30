package com.anotherrobbo.dm.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.anotherrobbo.dm.entity.CharacterRecord;
import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.external.MetadataService;
import com.anotherrobbo.dm.job.MatchJob;
import com.anotherrobbo.dm.job.MatchProcess;
import com.anotherrobbo.dm.model.ActivityStats;
import com.anotherrobbo.dm.model.PlayerStats;
import com.anotherrobbo.dm.model.TeamStats;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Singleton
public class MatchService {
	
	private Cache<String, MatchProcess> processCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
	private ExecutorService processExecutor = Executors.newCachedThreadPool();
	@Inject
	private PlayerService playerService;
	@Inject
	private PlayerRecordDao playerRecordDao;
	@Inject
	private Provider<MatchJob> matchJobProvider;
	@Inject
	private MetadataService metadataService;
	
	public MatchProcess findMatches(String system, String name, String name2) {
		MatchProcess process = new MatchProcess();
		process.setName1(name);
		process.setName2(name2);
		
		try {
			PlayerRecord player1 = playerService.getPlayerRecord(system, name);
			PlayerRecord player2 = playerService.getPlayerRecord(system, name2);
			
			if (player1 != null && player2 != null) {
				List<CharacterRecord> c1 = getCharacterRecords(player1);
				List<CharacterRecord> c2 = getCharacterRecords(player2);
				
				process.setTotal(c1.size() + c2.size());
				process.setName1(player1.getName());
				process.setName2(player2.getName());
				
				processExecutor.execute(matchJobProvider.get().initialise(process, player1, player2, false));
				
				processCache.put(process.getId(), process);
			} else if (player1 == null) {
				process.setError("Unable to find " + name);
			} else if (player2 == null) {
				process.setError("Unable to find " + name2);
			}
		} catch (BungieInterfaceException e) {
			process.setError(e.getMessage());
		}
		
		return process;
	}
	
	public MatchProcess pollProcess(String id) {
		return processCache.getIfPresent(id);
	}
	
	public ActivityStats getDetails(Long id) throws BungieInterfaceException {
		ActivityStats a = new ActivityStats();
        JsonNode act = BungieInterface.getActivityDetails(id);
        // Not all have teams
        if (act.hasNonNull("teams") && act.get("teams").iterator().hasNext()) {
        	a.setTeamStats(getTeamStats(act));
        } else {
        	a.setPlayerStats(getPlayerStats(act, null));
        }
        boolean useType = act.get("activityDetails").get("activityTypeHashOverride").intValue() > 0 && act.get("activityDetails").get("mode").asInt() != 4;
        a.setId(id);
        a.setPeriod(Timestamp.from(Instant.parse(act.get("period").textValue())));
        a.setActivityTypeHash(useType ? act.get("activityDetails").get("activityTypeHashOverride").asText() : null);
        a.setActivityHash(act.get("activityDetails").get("referenceId").asText());
        a.setPeriod(Timestamp.from(Instant.parse(act.get("period").textValue())));
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
	
	private List<CharacterRecord> getCharacterRecords(PlayerRecord player) throws BungieInterfaceException {
		JsonNode chars = BungieInterface.getPlayerChars(player.getSystemCode(), player.getId());
		for (JsonNode charNode : chars) {
			long characterId = charNode.get("characterId").asLong();
			CharacterRecord record = player.getCharacterRecord(characterId);
			if (record == null) {
				record = new CharacterRecord();
				record.setId(characterId);
				player.addCharacterRecord(record);
			}
			record.setActive(!charNode.get("deleted").asBoolean());
		}
		
		playerRecordDao.save(player);
		return player.getCharacterRecords();
	}
	
	private List<TeamStats> getTeamStats(JsonNode data) {
	    List<TeamStats> teams = new ArrayList<>();
	    for (JsonNode teamEntry : data.get("teams")) {
	    	TeamStats t = new TeamStats();
	        //@@log.info(act)
	        t.setId(teamEntry.get("teamId").asLong());
	        t.setName(teamEntry.get("teamName").asText());
	        t.setResult(teamEntry.get("standing").get("basic").get("displayValue").asText());
	        t.setScore(teamEntry.get("score").get("basic").get("displayValue").asText());
	        t.setPlayerStats(getPlayerStats(data, t.getId()));
	        teams.add(t);
		}
	    return teams;
	}
	
	private List<PlayerStats> getPlayerStats(JsonNode data, Long teamId) {
	    //@@log.info("Get player stats #{teamId}")
		List<PlayerStats> players = new ArrayList<>();
	    for (JsonNode playerEntry : data.get("entries")) {
	        if (teamId == null || playerEntry.get("values").get("team").get("basic").get("value").asLong() == teamId.longValue()) {
	            PlayerStats p = new PlayerStats();
	            p.setId(playerEntry.get("player").get("destinyUserInfo").get("membershipId").asLong());
	            p.setName(playerEntry.get("player").get("destinyUserInfo").get("displayName").asText());
	            p.setPlayerIcon(BungieInterface.BUNGIE_URL + playerEntry.get("player").get("destinyUserInfo").get("iconPath").asText());
	            // Looks like some old records are missing this!
	            if (playerEntry.get("player").hasNonNull("characterClass")) {
	            	p.setCharClass(playerEntry.get("player").get("characterClass").asText());
	            }
	            p.setLevel(playerEntry.get("player").get("characterLevel").asText());
	            p.setLight(playerEntry.get("player").get("lightLevel").asText());
	            p.setScoreVal(playerEntry.get("score").get("basic").get("value").asInt());
	            p.setScore(playerEntry.get("score").get("basic").get("displayValue").asText());
	            p.setK(playerEntry.get("values").get("kills").get("basic").get("displayValue").asText());
	            p.setKVal(playerEntry.get("values").get("kills").get("basic").get("value").asInt());
	            p.setA(playerEntry.get("values").get("assists").get("basic").get("displayValue").asText());
	            p.setD(playerEntry.get("values").get("deaths").get("basic").get("displayValue").asText());
	            p.setKd(playerEntry.get("values").get("killsDeathsRatio").get("basic").get("displayValue").asText());
	            p.setCompleted(playerEntry.get("values").get("completed").get("basic").get("value").asInt() == 1);
	            players.add(p);
	        }
	    }
	    // Sort by score then kills
	    players.stream().sorted(Comparator.comparingInt(PlayerStats::getScoreVal).thenComparing(Comparator.comparingInt(PlayerStats::getKVal)));
	    return players;
	}
	
	private String getDuration(JsonNode data) {
	    JsonNode playerEntry = data.get("entries").iterator().next();
	    String duration = playerEntry.get("values").get("activityDurationSeconds").get("basic").get("displayValue").asText();
	    return duration;
	}
	
}
