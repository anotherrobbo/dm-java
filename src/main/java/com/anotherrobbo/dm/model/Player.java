package com.anotherrobbo.dm.model;

import java.util.List;

public class Player {
    private String system;
    private int systemCode;
    private String systemIcon;
    private String name;
    private long id;
    private List<Character> chars;
    private String clan;
    private String clanTag;
    private int grimoire;
	
    public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public int getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(int systemCode) {
		this.systemCode = systemCode;
	}
	public String getSystemIcon() {
		return systemIcon;
	}
	public void setSystemIcon(String systemIcon) {
		this.systemIcon = systemIcon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Character> getChars() {
		return chars;
	}
	public void setChars(List<Character> chars) {
		this.chars = chars;
	}
	public String getClan() {
		return clan;
	}
	public void setClan(String clan) {
		this.clan = clan;
	}
	public String getClanTag() {
		return clanTag;
	}
	public void setClanTag(String clanTag) {
		this.clanTag = clanTag;
	}
	public int getGrimoire() {
		return grimoire;
	}
	public void setGrimoire(int grimoire) {
		this.grimoire = grimoire;
	}
}
