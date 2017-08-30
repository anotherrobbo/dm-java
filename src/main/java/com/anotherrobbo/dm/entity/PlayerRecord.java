package com.anotherrobbo.dm.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class PlayerRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Column(name="matches_count")
	private long matchesCount;

	private String name;

	@Column(name="overview_count")
	private long overviewCount;

	private String system;

	@Column(name="system_code")
	private Integer systemCode;

	//bi-directional many-to-one association to CharacterRecord
	@OneToMany(mappedBy="playerRecord", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<CharacterRecord> characterRecords;

	public PlayerRecord() {
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

	public List<CharacterRecord> getCharacterRecords() {
		if (this.characterRecords == null) {
			this.characterRecords = new ArrayList<CharacterRecord>();
		}
		return this.characterRecords;
	}

	public void setCharacterRecords(List<CharacterRecord> characterRecords) {
		this.characterRecords = characterRecords;
	}

	public CharacterRecord addCharacterRecord(CharacterRecord characterRecord) {
		getCharacterRecords().add(characterRecord);
		characterRecord.setPlayerRecord(this);

		return characterRecord;
	}

	public CharacterRecord removeCharacterRecord(CharacterRecord characterRecord) {
		getCharacterRecords().remove(characterRecord);
		characterRecord.setPlayerRecord(null);

		return characterRecord;
	}
	
	public CharacterRecord getCharacterRecord(Long characterId) {
		for (CharacterRecord record : getCharacterRecords()) {
			if (characterId.equals(record.getId())) {
				return record;
			}
		}
		return null;
	}

	public void incrementOverviewCount() {
		this.overviewCount++;
	}
	
	public void incrementMatchesCount() {
		this.matchesCount++;
	}

}