package com.anotherrobbo.dm.entity.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import com.anotherrobbo.dm.entity.PlayerRecord;

public class PlayerRecordDao {
	
	@Inject
	EntityManager em;
	
	public List<PlayerRecord> findAll() {
		TypedQuery<PlayerRecord> query = em.createNamedQuery("PlayerRecord.findAll", PlayerRecord.class);
		List<PlayerRecord> resultList = query.getResultList();
		return resultList;
	}

	public PlayerRecord findPlayer(String system, String name) {
		TypedQuery<PlayerRecord> query = em.createNamedQuery("PlayerRecord.findBySystemName", PlayerRecord.class);
		query.setParameter("system", StringUtils.upperCase(system));
		query.setParameter("name", StringUtils.upperCase(name));
		List<PlayerRecord> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}
	
	public void save(PlayerRecord pr) {
		em.persist(pr);
	}

}
