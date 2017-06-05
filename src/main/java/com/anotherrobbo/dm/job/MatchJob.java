package com.anotherrobbo.dm.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.jboss.logging.Logger;

import com.anotherrobbo.dm.entity.ActivityRecord;
import com.anotherrobbo.dm.entity.CharacterRecord;
import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.model.ActivityDetail;
import com.fasterxml.jackson.databind.JsonNode;

public class MatchJob implements Runnable {
	
	private final Logger log = Logger.getLogger(getClass());
	private final MatchProcess process;
	private final PlayerRecord p1;
	private final PlayerRecord p2;
	private final boolean forceCheck;
	
	public MatchJob(MatchProcess process, PlayerRecord p1, PlayerRecord p2, boolean forceCheck) {
		this.process = process;
		this.p1 = p1;
		this.p2 = p2;
		this.forceCheck = forceCheck;
	}

	@Override
	public void run() {
//		while (process.getProgress() < process.getTotal()) {
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			process.setProgress(process.getProgress() + 1);
//		}
		List<ActivityDetail> matches = new ArrayList<>();
		getGamesForAccount(p1);
		if (p1.getId().equals(p2.getId())) {
			log.info("Same id detected, getting matches with itself");
			getMatches(p1);
		} else {
			getGamesForAccount(p2);
			getMatches(p1, p2);
		}
		process.setResult(matches);
	}

	private void getGamesForAccount(PlayerRecord p) {
		int count = 250;
//        games = Hash.new
        for (CharacterRecord charRecord : p.getCharacterRecords()) {
        	List<ActivityRecord> activityRecords = charRecord.getActivityRecords();
            boolean refresh = true;
            if (!activityRecords.isEmpty()) {
                // lower count as we already have records and lower counts are quicker
                count = 50;
            }
//            else {
//                activityRecord = ActivityRecord.new
//                activityRecord.player_record_id = pr.id
//                activityRecord.id = char.id
//                activityRecord.activities = Hash.new
//            end
            
            // check if we already found records in the last 10 minutes
            Calendar tenMins = Calendar.getInstance();
            tenMins.add(Calendar.MINUTE, -10);
            ActivityRecord latest = activityRecords.isEmpty() ? null : activityRecords.get(0);
            if (latest != null && latest.getUpdatedAt().after(tenMins.getTime()) && !forceCheck) {
                log.info("Last updated less than 10 minutes ago so skipping load - " + activityRecords.get(0).getUpdatedAt());
        	} else {
                getGamesForChar(p.getSystemCode(), p.getId(), latest, count);
//                # Kick off a new job to save the activity record
//                SaveJob.perform_async(activityRecord)
            }
        	/*
            games.merge!(activityRecord.activities)
            # TODO sync this if we're doing it on multiple threads
            proc = Rails.cache.fetch(procId)
            proc.progress = proc.progress + 1
            Rails.cache.write(procId, proc, expires_in: 5.minutes)
        end*/
		}
        /*
        pr.increment(:matchesCount)
        # Kick off a new job to save the player record
        SaveJob.perform_async(pr)
        return games*/
	}

	private void getGamesForChar(Integer systemCode, Long pid, Long cid, ActivityRecord latest, int count) {
		// and now we assume that the ids are ALWAYS increasing... :S
        long max = 0;
        if (latest != null) {
            max = latest.getId();
            log.info("max = " + max);
        }
        int page = 0;
        while (true) {
            log.info(page + " - " + cid);
            //log.info(@@bungieURL + "/Platform/Destiny/Stats/ActivityHistory/#{systemCode}/#{id}/#{char.id}/?definitions=false&mode=None&page=#{page}&count=#{count}")
            JsonNode data = BungieInterface.getCharActivities(systemCode, pid, cid, page, count);
            // Break if we've reached a page with no data
            if (data.isNull()) {
                break;
            }
            long lastid = 0;
            Iterator<JsonNode> activities = data.elements();
            while (activities.hasNext()) {
            	JsonNode act = activities.next();
                lastid = act.get("activityDetails").get("instanceId").asLong();
                if (lastid <= max) {
                    break;
                }
                useType = act["activityDetails"]["activityTypeHashOverride"] > 0 && act["activityDetails"]["mode"] != 4
                ActivityRecord a = new ActivityRecord();
                a.setId(lastid);
                a.period = DateTime.parse(act["period"])
                a.activityTypeHash = useType ? act["activityDetails"]["activityTypeHashOverride"] : nil
                a.activityHash = act["activityDetails"]["referenceId"]
                a.result = act["values"]["standing"] != nil ? 1 - act["values"]["standing"]["basic"]["value"] : act["values"]["completed"]["basic"]["value"]
                a.team = act["values"]["team"] != nil ? act["values"]["team"]["basic"]["displayValue"][0] : nil
                a.kd = act["values"]["killsDeathsRatio"] != nil ? act["values"]["killsDeathsRatio"]["basic"]["displayValue"] : nil
                games[a.id] = a
        	}
            if (lastid <= max) {
                break;
        	}
            page++;
        }
	}

	private void getMatches(PlayerRecord p12) {
		// TODO Auto-generated method stub
		
	}
	
	private void getMatches(PlayerRecord p12, PlayerRecord p22) {
		// TODO Auto-generated method stub
		
	}
}

