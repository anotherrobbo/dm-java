package com.anotherrobbo.dm.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the player_records database table.
 * 
 */
@Entity
@Table(name="player_records")
@NamedQueries({
	@NamedQuery(name="PlayerRecord.findAll", query="SELECT p FROM PlayerRecord p"),
	@NamedQuery(name="PlayerRecord.findBySystemName", query="SELECT p FROM PlayerRecord p where upper(p.system) = :system AND upper(p.name) = :name")
})
public class PlayerRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="matches_count")
	private Long matchesCount;

	private String name;

	@Column(name="overview_count")
	private Long overviewCount;

	private String system;

	@Column(name="system_code")
	private Integer systemCode;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	//bi-directional many-to-one association to ActivityRecord
	@OneToMany(mappedBy="playerRecord")
	private List<ActivityRecord> activityRecords;

	public PlayerRecord() {
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Long getMatchesCount() {
		return this.matchesCount;
	}

	public void setMatchesCount(Long matchesCount) {
		this.matchesCount = matchesCount;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOverviewCount() {
		return this.overviewCount;
	}

	public void setOverviewCount(Long overviewCount) {
		this.overviewCount = overviewCount;
	}

	public String getSystem() {
		return this.system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public Integer getSystemCode() {
		return this.systemCode;
	}

	public void setSystemCode(Integer systemCode) {
		this.systemCode = systemCode;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<ActivityRecord> getActivityRecords() {
		return this.activityRecords;
	}

	public void setActivityRecords(List<ActivityRecord> activityRecords) {
		this.activityRecords = activityRecords;
	}

	public ActivityRecord addActivityRecord(ActivityRecord activityRecord) {
		getActivityRecords().add(activityRecord);
		activityRecord.setPlayerRecord(this);

		return activityRecord;
	}

	public ActivityRecord removeActivityRecord(ActivityRecord activityRecord) {
		getActivityRecords().remove(activityRecord);
		activityRecord.setPlayerRecord(null);

		return activityRecord;
	}

}