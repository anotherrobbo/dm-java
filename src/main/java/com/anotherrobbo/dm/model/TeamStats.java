package com.anotherrobbo.dm.model;

import java.util.List;

public class TeamStats {
	
	private Long id;
	private List<PlayerStats> playerStats;
    private String score;
    private String result;
    private String name;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<PlayerStats> getPlayerStats() {
		return playerStats;
	}
	public void setPlayerStats(List<PlayerStats> playerStats) {
		this.playerStats = playerStats;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
}
