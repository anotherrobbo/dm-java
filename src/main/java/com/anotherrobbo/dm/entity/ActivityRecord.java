package com.anotherrobbo.dm.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the activity_records database table.
 * 
 */
@Entity
@Table(name="activity_records")
@IdClass(ActivityRecordPk.class)
@NamedQuery(name="ActivityRecord.findAll", query="SELECT a FROM ActivityRecord a")
public class ActivityRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private Timestamp period;
	private String activityTypeHash;
	private String activityHash;
	private int result;
	private String team;
	private BigDecimal kd;

	//bi-directional many-to-one association to CharacterRecord
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="character_record_id", referencedColumnName="id")
	@Id
	private CharacterRecord characterRecord;
	
	public ActivityRecord() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Timestamp getPeriod() {
		return period;
	}
	
	public void setPeriod(Timestamp period) {
		this.period = period;
	}
	
	public String getActivityTypeHash() {
		return activityTypeHash;
	}

	public void setActivityTypeHash(String activityTypeHash) {
		this.activityTypeHash = activityTypeHash;
	}

	public String getActivityHash() {
		return activityHash;
	}

	public void setActivityHash(String activityHash) {
		this.activityHash = activityHash;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public BigDecimal getKd() {
		return kd;
	}

	public void setKd(BigDecimal kd) {
		this.kd = kd;
	}

	public CharacterRecord getCharacterRecord() {
		return characterRecord;
	}
	
	public void setCharacterRecord(CharacterRecord characterRecord) {
		this.characterRecord = characterRecord;
	}

}