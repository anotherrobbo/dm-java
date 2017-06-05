package com.anotherrobbo.dm.service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anotherrobbo.dm.entity.CharacterRecord;
import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.job.MatchJob;
import com.anotherrobbo.dm.job.MatchProcess;
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
	
	public MatchProcess findMatches(String system, String name, String name2) {
		MatchProcess process = new MatchProcess();
		process.setName1(name);
		process.setName2(name2);
		
		try {
			PlayerRecord player1 = playerService.getPlayerRecord(system, name);
			PlayerRecord player2 = playerService.getPlayerRecord(system, name2);
			
			if (player1 != null && player2 != null) {
				player1.incrementMatchesCount();
				player2.incrementMatchesCount();
				List<CharacterRecord> c1 = getCharacterRecords(player1);
				List<CharacterRecord> c2 = getCharacterRecords(player2);
				
				process.setTotal(c1.size() + c2.size());
				process.setName1(player1.getName());
				process.setName2(player2.getName());
				
				processExecutor.execute(new MatchJob(process, player1, player2, false));
				
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
	
	private List<CharacterRecord> getCharacterRecords(PlayerRecord player) throws BungieInterfaceException {
		JsonNode chars = BungieInterface.getPlayerChars(player.getSystemCode(), player.getId());
		Iterator<JsonNode> elements = chars.elements();
		while (elements.hasNext()) {
			JsonNode charNode = elements.next();
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
	
}
