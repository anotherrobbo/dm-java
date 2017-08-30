package com.anotherrobbo.dm.entity.dao;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.anotherrobbo.dm.entity.CharacterRecord;
import com.google.inject.persist.Transactional;

public class CharacterRecordDao {
	
	@Inject
	Provider<EntityManager> em;
	
	@Transactional
	public void save(CharacterRecord cr) {
		em.get().merge(cr);
	}

}
