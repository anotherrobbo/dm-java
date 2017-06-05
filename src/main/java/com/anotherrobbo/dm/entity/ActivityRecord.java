package com.anotherrobbo.dm.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
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
@NamedQuery(name="ActivityRecord.findAll", query="SELECT a FROM ActivityRecord a")
public class ActivityRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private Timestamp period;

	//bi-directional many-to-one association to PlayerRecord
	@ManyToOne
	@JoinColumn(name="character_record_id", referencedColumnName="id")
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

	public CharacterRecord getCharacterRecord() {
		return characterRecord;
	}
	
	public void setCharacterRecord(CharacterRecord characterRecord) {
		this.characterRecord = characterRecord;
	}

}