package com.anotherrobbo.dm.job;

import java.util.List;

import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.ActivityRecordDao;
import com.anotherrobbo.dm.entity.dao.CharacterRecordDao;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.external.MetadataService;

public class BulkLoadJob extends GameJob<BulkLoadProcess> {

    private boolean initialised;
    private List<PlayerRecord> players;
    
    BulkLoadJob(MetadataService metadataService, PlayerRecordDao playerRecordDao, CharacterRecordDao characterRecordDao, ActivityRecordDao activityRecordDao) {
        super(metadataService, playerRecordDao, characterRecordDao, activityRecordDao);
    }
    
    public BulkLoadJob initialise(BulkLoadProcess process, List<PlayerRecord> players) {
        this.process = process;
        this.players = players;
        this.initialised = true;
        return this;
    }
    
    @Override
    public void run() {
        if (!initialised) {
            throw new RuntimeException("Must be initialised first!");
        }
        for (PlayerRecord player : players) {
            process.setCurrent(player.getName());
            try {
                getGamesForAccount(player);
            } catch (BungieInterfaceException e) {
                process.setError("Unable to bulk load: " + e.getMessage());
                break;
            }
        }
        process.setRunning(false);
    }
    
}