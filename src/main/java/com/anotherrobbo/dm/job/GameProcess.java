package com.anotherrobbo.dm.job;

import java.util.UUID;

public abstract class GameProcess {
	private final String id = UUID.randomUUID().toString();
	private String error;
	
	public String getId() {
		return id;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}