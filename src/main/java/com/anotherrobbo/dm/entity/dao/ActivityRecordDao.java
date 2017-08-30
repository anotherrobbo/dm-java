package com.anotherrobbo.dm.entity.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.anotherrobbo.dm.entity.ActivityRecord;

public class ActivityRecordDao {
	
	@Inject
	Provider<EntityManager> em;
	
	public List<ActivityRecordMatch> getActivitiesForPlayer(long pid) {
		EntityManager entityManager = em.get();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ActivityRecord> query = cb.createQuery(ActivityRecord.class);
		
		Root<ActivityRecord> root = query.from(ActivityRecord.class);
		Join<Object, Object> character = root.join("characterRecord");
		Join<Object, Object> player = character.join("playerRecord");
		
		query.select(root);
		query.where(cb.equal(player.get("id"), pid));
		query.orderBy(cb.desc(root.get("period")));
		List<ActivityRecord> list = entityManager.createQuery(query).getResultList();
		List<ActivityRecordMatch> resultList = new ArrayList<>();
		for (ActivityRecord record : list) {
		    resultList.add(new ActivityRecordMatch(record));
		}
		return resultList;
	}
	
    public List<ActivityRecordMatch> getActivitiesForPlayers(long pid1, long pid2) {
        EntityManager entityManager = em.get();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        
        Root<ActivityRecord> root = query.from(ActivityRecord.class);
        Join<Object, Object> character = root.join("characterRecord");
        Join<Object, Object> player = character.join("playerRecord");
        
        Root<ActivityRecord> subroot = query.from(ActivityRecord.class);
        Join<Object, Object> subcharacter = subroot.join("characterRecord");
        Join<Object, Object> subplayer = subcharacter.join("playerRecord");
        
        query.multiselect(root, subroot);
        query.where(cb.equal(player.get("id"), pid1),
                    cb.equal(subplayer.get("id"), pid2),
                    cb.equal(root.get("id"), subroot.get("id")));
        query.orderBy(cb.desc(root.get("period")));
        entityManager.createQuery(query).getResultList();
        List<Tuple> list = entityManager.createQuery(query).getResultList();
        List<ActivityRecordMatch> resultList = new ArrayList<>();
        for (Tuple tuple : list) {
            resultList.add(new ActivityRecordMatch(tuple.get(0, ActivityRecord.class), tuple.get(1, ActivityRecord.class)));
        }
        return resultList;
    }
    
    public static class ActivityRecordMatch {
        
        private ActivityRecord record;
        private boolean sameTeam;
        
        public ActivityRecordMatch(ActivityRecord record) {
            this.record = record;
            this.sameTeam = true;
        }
        
        public ActivityRecordMatch(ActivityRecord record, ActivityRecord otherRecord) {
            this.record = record;
            this.sameTeam = StringUtils.equalsIgnoreCase(record.getTeam(), otherRecord.getTeam());
        }
        
        public ActivityRecord getRecord() {
            return record;
        }
        
        public boolean isSameTeam() {
            return sameTeam;
        }
    }

}
