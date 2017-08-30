package com.anotherrobbo.dm.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public abstract class Activity {
	private Long id;
    private Timestamp period;
    private String activityTypeHash;
    private String activityHash;
    private boolean result;
    private String team;
    private BigDecimal kd;
    private String activityIcon;
    private String activityType;
    private String activityName;
    private boolean sameTeam;
    
	public Long getId() {
		return id;
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
	public String getActivityTypeHash() {
		return activityTypeHash;
	}
	public void setActivityTypeHash(String activityTypeHash) {
		this.activityTypeHash = activityTypeHash;
	}
	public String getActivityHash() {
		return activityHash;
	}
	public void setActivityHash(String activityHash) {
		this.activityHash = activityHash;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public void setResult(int result) {
		this.result = result > 0;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public BigDecimal getKd() {
		return kd;
	}
	public void setKd(BigDecimal kd) {
		this.kd = kd;
	}
	public String getActivityIcon() {
		return activityIcon;
	}
	public void setActivityIcon(String activityIcon) {
		this.activityIcon = activityIcon;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public boolean isSameTeam() {
		return sameTeam;
	}
	public void setSameTeam(boolean sameTeam) {
		this.sameTeam = sameTeam;
	}
}
