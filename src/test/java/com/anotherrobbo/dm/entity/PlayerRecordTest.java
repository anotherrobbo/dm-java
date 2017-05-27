package com.anotherrobbo.dm.entity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.anotherrobbo.dm.entity.ActivityRecord;
import com.anotherrobbo.dm.entity.PlayerRecord;

public class PlayerRecordTest {
	
	Logger log = Logger.getLogger(PlayerRecordTest.class);

//	@Test
	public void testGetAll() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dm");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		TypedQuery<PlayerRecord> query = entityManager.createNamedQuery("PlayerRecord.findAll", PlayerRecord.class);
		List<PlayerRecord> resultList = query.getResultList();
		Assert.assertNotNull(resultList);
		Assert.assertFalse(resultList.isEmpty());
		for (PlayerRecord pr : resultList) {
			log.info(pr.getName());
		}
	}
	
	@Test
	public void testGetARs() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dm");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		PlayerRecord record = entityManager.find(PlayerRecord.class, 4611686018450420334L);
		Assert.assertNotNull(record);
		Assert.assertNotNull(record.getActivityRecords());
		Assert.assertFalse(record.getActivityRecords().isEmpty());
		for (ActivityRecord ar : record.getActivityRecords()) {
			log.info(ar.getId());
		}
	}
	
}
