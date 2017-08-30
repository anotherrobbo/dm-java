package com.anotherrobbo.dm.job;

public class BulkLoadProcess extends GameProcess {
	private String current;
	private boolean running = true;
	
	public String getCurrent() {
        return current;
    }
	public void setCurrent(String current) {
        this.current = current;
    }
	public boolean isRunning() {
        return running;
    }
	public void setRunning(boolean running) {
        this.running = running;
    }
}