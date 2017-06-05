package com.anotherrobbo.dm.job;

import java.util.List;
import java.util.UUID;

import com.anotherrobbo.dm.model.ActivityDetail;

public class MatchProcess {
	private final String id = UUID.randomUUID().toString();
	private String name1;
	private String name2;
	int total;
	int progress;
	private List<ActivityDetail> result;
	private String error;
	
	public String getId() {
		return id;
	}
	
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public List<ActivityDetail> getResult() {
		return result;
	}
	public void setResult(List<ActivityDetail> result) {
		this.result = result;
	}
}