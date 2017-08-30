package com.anotherrobbo.dm.model;

public class PlayerStats {
    
	private Long id;
	private boolean completed;
    private String kd;
    private String d;
    private String a;
    // Need a numeric value for sorting
    private int kVal;
    private String k;
    // Need a numeric value for sorting
    private int scoreVal;
    private String score;
    private String light;
    private String level;
    private String charClass;
    private String playerIcon;
    private String name;
    
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getKd() {
		return kd;
	}
	public void setKd(String kd) {
		this.kd = kd;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public int getKVal() {
		return kVal;
	}
	public void setKVal(int kVal) {
		this.kVal = kVal;
	}
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public int getScoreVal() {
		return scoreVal;
	}
	public void setScoreVal(int scoreVal) {
		this.scoreVal = scoreVal;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getLight() {
		return light;
	}
	public void setLight(String light) {
		this.light = light;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getCharClass() {
		return charClass;
	}
	public void setCharClass(String charClass) {
		this.charClass = charClass;
	}
	public String getPlayerIcon() {
		return playerIcon;
	}
	public void setPlayerIcon(String playerIcon) {
		this.playerIcon = playerIcon;
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
}
