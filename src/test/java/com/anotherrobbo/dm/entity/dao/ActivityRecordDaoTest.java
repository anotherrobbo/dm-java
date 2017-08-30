package com.anotherrobbo.dm.entity.dao;

import java.util.List;

import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.anotherrobbo.dm.entity.ActivityRecord;
import com.anotherrobbo.dm.entity.dao.ActivityRecordDao.ActivityRecordMatch;
import com.anotherrobbo.dm.entity.manager.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ActivityRecordDaoTest {

	Logger log = Logger.getLogger(getClass());
	
	private static Injector injector = Guice.createInjector(new JpaPersistModule("dm").properties(EntityManagerProvider.getPropertyOverrides()));
	
	@BeforeClass
	public static void setup() {
		injector.getInstance(PersistService.class).start();
	}
	
	@Test
	public void testGetActivitiesForPlayer() {
		ActivityRecordDao dao = injector.getInstance(ActivityRecordDao.class);
		long pid = 4611686018450420334L;
		List<ActivityRecordMatch> activitiesForPlayer = dao.getActivitiesForPlayer(pid);
		Assert.assertNotNull(activitiesForPlayer);
		Assert.assertFalse(activitiesForPlayer.isEmpty());
		log.info("Found " + activitiesForPlayer.size() + " activities");
		for (ActivityRecordMatch match : activitiesForPlayer) {
		    ActivityRecord rec = match.getRecord();
			Assert.assertEquals(Long.valueOf(pid), rec.getCharacterRecord().getPlayerRecord().getId());
		}
	}
	
	@Test
    public void testGetActivitiesForPlayers() {
        ActivityRecordDao dao = injector.getInstance(ActivityRecordDao.class);
        long pid1 = 4611686018450420334L;
        long pid2 = 4611686018451796042L;
        List<ActivityRecordMatch> activitiesForPlayer = dao.getActivitiesForPlayers(pid1, pid2);
        Assert.assertNotNull(activitiesForPlayer);
        Assert.assertFalse(activitiesForPlayer.isEmpty());
        log.info("Found " + activitiesForPlayer.size() + " activities");
        for (ActivityRecordMatch match : activitiesForPlayer) {
            ActivityRecord rec = match.getRecord();
            Assert.assertEquals(Long.valueOf(pid1), rec.getCharacterRecord().getPlayerRecord().getId());
        }
    }
	
}
