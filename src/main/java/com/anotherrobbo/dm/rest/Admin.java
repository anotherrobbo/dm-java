package com.anotherrobbo.dm.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.anotherrobbo.dm.entity.PlayerRecord;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.freemarker.FreemarkerUtil;

@Path("/admin")
public class Admin {
	
	@Inject
	private FreemarkerUtil freemarkerUtil;
	
	@Inject
	private PlayerRecordDao playerRecordDao;
	
	@GET
	@Path("/playerStats")
	public Response getPlayerStats() {
		Map<String, Object> dataModel = new HashMap<>();
		List<PlayerRecord> resultList = playerRecordDao.findAll();
		dataModel.put("records", resultList);
		return freemarkerUtil.render(dataModel, "/admin/playerStats.ftl");
	}
	
}
