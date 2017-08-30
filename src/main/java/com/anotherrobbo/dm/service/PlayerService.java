package com.anotherrobbo.dm.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.MetadataService;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.model.Player;
import com.fasterxml.jackson.databind.JsonNode;

public class PlayerService {
	
	@Inject
	private PlayerRecordDao playerRecordDao;
	@Inject
	private MetadataService metadataService;
	
	public Player getPlayerOverview(String system, String name) throws BungieInterfaceException {
		PlayerRecord pr = getPlayerRecord(system, name);
		
		Player p = new Player();
		p.setId(pr.getId());
		p.setSystem(pr.getSystem());
		p.setSystemCode(pr.getSystemCode());
		p.setName(pr.getName());
		
		JsonNode summary = BungieInterface.getPlayerSummary(p.getSystemCode(), p.getId());
		// No clan info available in D1 any more :(
//		JsonNode clan = getClan(summary);
//		if (clan != null) {
//	        p.setClan(clan.get("name").asText());
//	        p.setClanTag(clan.get("clanCallsign").asText());
//		}
        p.setGrimoire(summary.get("data").get("grimoireScore").asInt());
        p.setChars(getActiveChars(summary));
        pr.incrementOverviewCount();
        playerRecordDao.save(pr);
        return p;
	}
	
	PlayerRecord getPlayerRecord(String system, String name) {
		PlayerRecord pr = playerRecordDao.findPlayer(system, name);
        if (pr == null) {
            pr = new PlayerRecord();
            pr.setSystem(system);
            pr.setSystemCode(getSystemCode(system));
            
			try {
				JsonNode info = BungieInterface.getPlayerInfo(pr.getSystemCode(), name);
	            if (info != null) {
	                pr.setName(info.get("displayName").asText());
	                pr.setId(info.get("membershipId").asLong());
	                playerRecordDao.save(pr);
	            } else {
	                pr = null;
	            }
			} catch (BungieInterfaceException e) {
				e.printStackTrace();
				pr = null;
			}
        }
        
		return pr;
	}
	
//	private JsonNode getClan(JsonNode summary) {
	    // Clans no longer working for D1
//		JsonNode clan = summary.get("clans").get(0);
//		if (clan != null) {
//			String clanId = clan.get("groupId").asText();
//			return summary.get("relatedGroups").get(clanId);
//		} else {
//			return null;
//		}
//	}
	
	private List<com.anotherrobbo.dm.model.Character> getActiveChars(JsonNode summary) {
		JsonNode characters = summary.get("data").get("characters");
		List<com.anotherrobbo.dm.model.Character> chars = new ArrayList<>();
		Iterator<JsonNode> elements = characters.elements();
		while (elements.hasNext()) {
			chars.add(createChar(elements.next()));
		}
	    return chars;
	}
	
	private com.anotherrobbo.dm.model.Character createChar(JsonNode theChar) {
		com.anotherrobbo.dm.model.Character c = new com.anotherrobbo.dm.model.Character();
	    JsonNode charBase = theChar.get("characterBase");
        c.setId(charBase.get("characterId").asLong());
	    c.setCharClass(metadataService.getDef("class", "className", charBase.get("classHash").asText()));
	    c.setRace(metadataService.getDef("race", "raceName", charBase.get("raceHash").asText()));
	    c.setGender(metadataService.getDef("gender", "genderName", charBase.get("genderHash").asText()));
	    c.setLight(charBase.get("powerLevel").asInt());
	    c.setLevel(theChar.get("characterLevel").asInt());
	    c.setEmblem(BungieInterface.BUNGIE_URL + theChar.get("emblemPath").asText());
	    c.setBg(BungieInterface.BUNGIE_URL + theChar.get("backgroundPath").asText());
	    return c;
	}
	
	private int getSystemCode(String system) {
		switch (system) {
			case "ps":
		        return 2;
			case "xb":
		        return 1;
		    default:
		    	return 0;
		}
	}
}
