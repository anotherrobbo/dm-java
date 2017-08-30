package com.anotherrobbo.dm.web;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Application;

import com.anotherrobbo.dm.external.MetadataService;
import com.anotherrobbo.dm.job.MatchJob;
import com.anotherrobbo.dm.job.MatchJobProvider;
import com.anotherrobbo.dm.rest.Admin;
import com.anotherrobbo.dm.rest.Index;
import com.anotherrobbo.dm.rest.Matches;
import com.anotherrobbo.dm.rest.Overview;
import com.anotherrobbo.dm.rest.Resources;
import com.anotherrobbo.dm.rest.Search;
import com.google.inject.AbstractModule;

public class InjectionModule extends AbstractModule {
	
	private ServletContext context;
	
	public InjectionModule(ServletContext context) {
		this.context = context;
	}
	
	@Override
	protected void configure() {
	    bind(Application.class);
		bind(Index.class);
		bind(Search.class);
		bind(Overview.class);
		bind(Matches.class);
		bind(Admin.class);
		bind(Resources.class);
		bind(ServletContext.class).toInstance(context);
		bind(MetadataService.class);
		bind(MatchJob.class).toProvider(MatchJobProvider.class);
	}
}