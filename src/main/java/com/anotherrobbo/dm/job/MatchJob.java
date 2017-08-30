package com.anotherrobbo.dm.job;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import com.anotherrobbo.dm.entity.ActivityRecord;
import com.anotherrobbo.dm.entity.CharacterRecord;
import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.ActivityRecordDao;
import com.anotherrobbo.dm.entity.dao.ActivityRecordDao.ActivityRecordMatch;
import com.anotherrobbo.dm.entity.dao.CharacterRecordDao;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.external.BungieInterface;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.external.MetadataService;
import com.anotherrobbo.dm.model.ActivityStats;
import com.fasterxml.jackson.databind.JsonNode;

public class MatchJob implements Runnable {
    
    private final Logger log = Logger.getLogger(getClass());
    private boolean initialised;
    private MatchProcess process;
    private PlayerRecord p1;
    private PlayerRecord p2;
    private boolean forceCheck;
    
    private MetadataService metadataService;
    private PlayerRecordDao playerRecordDao;
    private CharacterRecordDao characterRecordDao;
    private ActivityRecordDao activityRecordDao;
    
    MatchJob(MetadataService metadataService, PlayerRecordDao playerRecordDao, CharacterRecordDao characterRecordDao, ActivityRecordDao activityRecordDao) {
        this.metadataService = metadataService;
        this.playerRecordDao = playerRecordDao;
        this.characterRecordDao = characterRecordDao;
        this.activityRecordDao = activityRecordDao;
    }
    
    public MatchJob initialise(MatchProcess process, PlayerRecord p1, PlayerRecord p2, boolean forceCheck) {
        this.process = process;
        this.p1 = p1;
        this.p2 = p2;
        this.forceCheck = forceCheck;
        this.initialised = true;
        return this;
    }

    @Override
    public void run() {
        if (!initialised) {
            throw new RuntimeException("Must be initialised first!");
        }
        try {
            List<ActivityStats> matches = new ArrayList<>();
            getGamesForAccount(p1);
            if (p1.getId().equals(p2.getId())) {
                log.info("Same id detected, getting matches with itself");
                matches = getMatches(p1);
            } else {
                getGamesForAccount(p2);
                matches = getMatches(p1, p2);
            }
            process.setResult(matches);
        } catch (BungieInterfaceException e) {
            process.setError("Unable to load matches: " + e.getMessage());
        }
    }

    private void getGamesForAccount(PlayerRecord p) throws BungieInterfaceException {
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
//                # Kick off a new job to save the activity record
//                SaveJob.perform_async(activityRecord)
            }
            process.incrementProgress();
            /*
            games.merge!(activityRecord.activities)
            # TODO sync this if we're doing it on multiple threads
            proc = Rails.cache.fetch(procId)
            proc.progress = proc.progress + 1
            Rails.cache.write(procId, proc, expires_in: 5.minutes)
        end*/
        }
        p.incrementMatchesCount();
        playerRecordDao.save(p);
        /*
        pr.increment(:matchesCount)
        # Kick off a new job to save the player record
        SaveJob.perform_async(pr)
        return games*/
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
            for (JsonNode act : data) {
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

    private List<ActivityStats> getMatches(PlayerRecord p) {
        List<ActivityRecordMatch> activities = activityRecordDao.getActivitiesForPlayer(p.getId());
        return convert(activities);
    }
    
    private List<ActivityStats> getMatches(PlayerRecord p1, PlayerRecord p2) {
        List<ActivityRecordMatch> activities = activityRecordDao.getActivitiesForPlayers(p1.getId(), p2.getId());
        return convert(activities);
    }
    
    private List<ActivityStats> convert(List<ActivityRecordMatch> activitieMatches) {
        List<ActivityStats> detailsList = new ArrayList<ActivityStats>();
        for (ActivityRecordMatch m : activitieMatches) {
            ActivityRecord g = m.getRecord();
            ActivityStats a = new ActivityStats();
            a.setId(g.getId());
            a.setPeriod(g.getPeriod());
            a.setActivityTypeHash(g.getActivityTypeHash());
            a.setActivityHash(g.getActivityHash());
            a.setActivityName(metadataService.getDef("activity", "activityName", a.getActivityHash()));
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
            a.setResult(g.getResult());
            a.setTeam(g.getTeam());
            a.setKd(g.getKd());
            a.setSameTeam(m.isSameTeam());
            detailsList.add(a);
        }
        return detailsList;
    }
}

