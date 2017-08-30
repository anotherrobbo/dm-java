package com.anotherrobbo.dm.model;

import java.util.List;

public class ActivityStats extends Activity {
	
	private List<TeamStats> teamStats;
	private List<PlayerStats> playerStats;
	private boolean completed;
	private String duration;
	
	public List<TeamStats> getTeamStats() {
		return teamStats;
	}
	public void setTeamStats(List<TeamStats> teamStats) {
		this.teamStats = teamStats;
	}
	public List<PlayerStats> getPlayerStats() {
		return playerStats;
	}
	public void setPlayerStats(List<PlayerStats> playerStats) {
		this.playerStats = playerStats;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
}
