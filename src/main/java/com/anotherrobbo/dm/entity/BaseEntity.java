package com.anotherrobbo.dm.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="created_at")
	private Timestamp createdAt = Timestamp.from(Instant.now());

	@Column(name="updated_at")
	private Timestamp updatedAt = Timestamp.from(Instant.now());
	
	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@PrePersist
	public void onSave() {
		Timestamp now = Timestamp.from(Instant.now());
		this.createdAt = now;
		this.updatedAt = now;
	}
	
	@PreUpdate
	public void onUpdate() {
		Timestamp now = Timestamp.from(Instant.now());
		this.updatedAt = now;
	}
}
