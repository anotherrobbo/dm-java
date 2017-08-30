package com.anotherrobbo.dm.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


/**
 * The persistent class for the character_records database table.
 * 
 */
@Entity
@Table(name="character_records")
@NamedQueries({
	@NamedQuery(name="CharacterRecord.findAll", query="SELECT c FROM CharacterRecord c"),
	@NamedQuery(name="CharacterRecord.findByPlayer", query="SELECT c FROM CharacterRecord c where c.playerRecord = :player")
})
public class CharacterRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private boolean active;

	//bi-directional many-to-one association to PlayerRecord
	@ManyToOne
	@JoinColumn(name="player_record_id", referencedColumnName="id")
	private PlayerRecord playerRecord;
	
	//bi-directional many-to-one association to ActivityRecord
	@OneToMany(mappedBy="characterRecord", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@OrderBy("period desc")
	private List<ActivityRecord> activityRecords;

	public CharacterRecord() {
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public PlayerRecord getPlayerRecord() {
		return playerRecord;
	}
	
	public void setPlayerRecord(PlayerRecord playerRecord) {
		this.playerRecord = playerRecord;
	}
	
	public List<ActivityRecord> getActivityRecords() {
		if (this.activityRecords == null) {
			this.activityRecords = new ArrayList<ActivityRecord>();
		}
		return this.activityRecords;
	}

	public void setActivityRecords(List<ActivityRecord> activityRecords) {
		this.activityRecords = activityRecords;
	}

	public ActivityRecord addActivityRecord(ActivityRecord activityRecord) {
		getActivityRecords().add(activityRecord);
		activityRecord.setCharacterRecord(this);

		return activityRecord;
	}

	public ActivityRecord removeActivityRecord(ActivityRecord activityRecord) {
		getActivityRecords().remove(activityRecord);
		activityRecord.setCharacterRecord(null);

		return activityRecord;
	}

}