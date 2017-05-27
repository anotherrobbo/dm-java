package com.anotherrobbo.dm.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
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
public class ActivityRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String activities;

	@Column(name="created_at")
	private Timestamp createdAt;


	@Column(name="updated_at")
	private Timestamp updatedAt;

	//bi-directional many-to-one association to PlayerRecord
	@ManyToOne
	@JoinColumn(name="player_record_id", referencedColumnName="id")
	private PlayerRecord playerRecord;

	public ActivityRecord() {
	}

	public String getActivities() {
		return this.activities;
	}

	public void setActivities(String activities) {
		this.activities = activities;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public PlayerRecord getPlayerRecord() {
		return this.playerRecord;
	}

	public void setPlayerRecord(PlayerRecord playerRecord) {
		this.playerRecord = playerRecord;
	}

}