package com.anotherrobbo.dm.job;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

import org.jboss.logging.Logger;

import com.anotherrobbo.dm.entity.ActivityRecord;
import com.anotherrobbo.dm.entity.CharacterRecord;
import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.ActivityRecordDao;
import com.anotherrobbo.dm.entity.dao.CharacterRecordDao;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.external.MetadataService;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class GameJob<P extends GameProcess> implements Runnable {
    
    protected final Logger log = Logger.getLogger(getClass());
    protected P process;
    protected boolean forceCheck;
    
    protected MetadataService metadataService;
    protected PlayerRecordDao playerRecordDao;
    protected CharacterRecordDao characterRecordDao;
    protected ActivityRecordDao activityRecordDao;
    
    GameJob(MetadataService metadataService, PlayerRecordDao playerRecordDao, CharacterRecordDao characterRecordDao, ActivityRecordDao activityRecordDao) {
        this.metadataService = metadataService;
        this.playerRecordDao = playerRecordDao;
        this.characterRecordDao = characterRecordDao;
        this.activityRecordDao = activityRecordDao;
    }
    
    protected void getGamesForAccount(PlayerRecord p) throws BungieInterfaceException {
        int count = 250;
        for (CharacterRecord charRecord : p.getCharacterRecords()) {
            List<ActivityRecord> activityRecords = charRecord.getActivityRecords();
            if (!activityRecords.isEmpty()) {
                // lower count as we already have records and lower counts are quicker
                count = 50;
            }
            
            // check if we already found records in the last 10 minutes
            Instant tenMins = Instant.now().minus(10, ChronoUnit.MINUTES);
            ActivityRecord latest = activityRecords.isEmpty() ? null : activityRecords.get(0);
            if (latest != null && latest.getUpdatedAt().toInstant().isAfter(tenMins) && !forceCheck) {
                log.info("Last updated less than 10 minutes ago so skipping load - " + activityRecords.get(0).getUpdatedAt());
            } else {
                getGamesForChar(p.getSystemCode(), p.getId(), charRecord, latest, count);
            }
            doPostChar(process, charRecord);
        }
        p.incrementMatchesCount();
        playerRecordDao.save(p);
    }
    
    protected void doPostChar(P process, CharacterRecord charRecord) {
        // Override to do something here.
    }

    private void getGamesForChar(Integer systemCode, Long pid, CharacterRecord charRecord, ActivityRecord latest, int count) throws BungieInterfaceException {
        // and now we assume that the ids are ALWAYS increasing... :S
        long max = 0;
        if (latest != null) {
            max = latest.getId();
            log.info("max = " + max);
        }
        int page = 0;
        while (true) {
            log.info(page + " - " + charRecord.getId());
            //log.info(@@bungieURL + "/Platform/Destiny/Stats/ActivityHistory/#{systemCode}/#{id}/#{char.id}/?definitions=false&mode=None&page=#{page}&count=#{count}")
            JsonNode data = BungieInterface.getCharActivities(systemCode, pid, charRecord.getId(), page, count);
            // Break if we've reached a page with no data
            if (data == null) {
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
                boolean useType = act.get("activityDetails").get("activityTypeHashOverride").intValue() > 0 && act.get("activityDetails").get("mode").asInt() != 4;
                ActivityRecord a = new ActivityRecord();
                a.setId(lastid);
                a.setPeriod(Timestamp.from(Instant.parse(act.get("period").textValue())));
                a.setActivityTypeHash(useType ? act.get("activityDetails").get("activityTypeHashOverride").asText() : null);
                a.setActivityHash(act.get("activityDetails").get("referenceId").asText());
                a.setResult(act.get("values").hasNonNull("standing") ? 1 - act.get("values").get("standing").get("basic").get("value").asInt() : act.get("values").get("completed").get("basic").get("value").asInt());
                a.setTeam(act.get("values").hasNonNull("team") ? act.get("values").get("team").get("basic").get("displayValue").asText() : null);
                a.setKd(act.get("values").hasNonNull("killsDeathsRatio") ? act.get("values").get("killsDeathsRatio").get("basic").get("value").decimalValue() : null);
                charRecord.addActivityRecord(a);
            }
            if (lastid <= max) {
                break;
            }
            page++;
        }
        characterRecordDao.save(charRecord);
    }
}

