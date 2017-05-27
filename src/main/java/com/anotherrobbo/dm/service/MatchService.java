package com.anotherrobbo.dm.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.concurrent.ConcurrentUtils;

import com.anotherrobbo.dm.external.BungieInterface.BungieInterfaceException;
import com.anotherrobbo.dm.model.Player;

@Singleton
public class MatchService {
	
	private Map<String, MatchProcess> processMap = new HashMap<>();
	private ExecutorService processExecutor = Executors.newCachedThreadPool();
	@Inject
	private PlayerService playerService;
	
	public MatchProcess findMatches(String system, String name, String name2) {
		MatchProcess process = new MatchProcess();
		process.setName1(name);
		process.setName2(name2);
		
		try {
			Player player1 = playerService.getPlayer(system, name);
			Player player2 = playerService.getPlayer(system, name2);
			
			if (player1 != null && player2 != null) {
				process.setTotal(player1.getChars().size() + player2.getChars().size());
				process.setName1(player1.getName());
				process.setName2(player2.getName());
				
				processExecutor.execute(new MatchProcessTask(process, player1, player2, false));
				
				processMap.put(process.getId(), process);
			} else if (player1 == null) {
				process.setError("Unable to find " + name);
			} else if (player2 == null) {
				process.setError("Unable to find " + name2);
			}
		} catch (BungieInterfaceException e) {
			process.setError(e.getMessage());
		}
		
		return process;
	}
	
	public MatchProcess pollProcess(String id) {
		return processMap.get(id);
	}
	
	public static class MatchProcess {
		private final String id = UUID.randomUUID().toString();
		private String name1;
		private String name2;
		private int total;
		private int progress;
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
	}
	
	private class MatchProcessTask implements Runnable {
		private final MatchProcess process;
		private final Player p1;
		private final Player p2;
		private final boolean forceCheck;
		
		public MatchProcessTask(MatchProcess process, Player p1, Player p2, boolean forceCheck) {
			this.process = process;
			this.p1 = p1;
			this.p2 = p2;
			this.forceCheck = forceCheck;
		}

		@Override
		public void run() {
			while (process.progress < process.total) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				process.setProgress(process.progress + 1);
			}
		}
	}

}
