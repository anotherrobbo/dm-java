package com.anotherrobbo.dm.service;

import javax.inject.Inject;

import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.model.Player;

public class PlayerService {
	
	@Inject
	PlayerRecordDao playerRecordDao;
	
	public Player getPlayer(String system, String name) {
		PlayerRecord pr = getPlayerRecord(system, name);
		
		Player p = new Player();
		p.setId(pr.getId());
		p.setSystem(pr.getSystem());
		p.setSystemCode(pr.getSystemCode());
		p.setName(pr.getName());
		
//		summary = getSummaryData(p.systemCode, p.id);
//		        
//        p.clan = summary["clanName"];
//        p.clanTag = summary["clanTag"];
//        p.grimoire = summary["grimoireScore"];
//        p.chars = getChars(summary);
        return p;
	}
	
	private PlayerRecord getPlayerRecord(String system, String name) {
		PlayerRecord pr = playerRecordDao.findPlayer(system, name);
        if (pr == null) {
//            pr = new PlayerRecord();
//            pr.setSystem(system);
//            pr.setSystemCode(getSystemCode(system));
//            
//            info = getInfo(pr.systemCode, name);
//            if (info != null) {
//                pr.name = info["displayName"]
//                pr.id = info["membershipId"]
//                pr.save;
//            } else {
//                pr == null;
//            }
        }
		        
		return pr;
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
