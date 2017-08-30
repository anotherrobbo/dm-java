package com.anotherrobbo.dm.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ActivityRecordPk implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private CharacterRecord characterRecord;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CharacterRecord getCharacterRecord() {
		return characterRecord;
	}
	public void setCharacterRecord(CharacterRecord characterRecord) {
		this.characterRecord = characterRecord;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((characterRecord == null) ? 0 : characterRecord.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityRecordPk other = (ActivityRecordPk) obj;
		if (characterRecord == null) {
			if (other.characterRecord != null)
				return false;
		} else if (!characterRecord.equals(other.characterRecord))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}