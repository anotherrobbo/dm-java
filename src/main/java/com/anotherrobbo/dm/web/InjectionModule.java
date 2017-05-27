package com.anotherrobbo.dm.web;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import com.anotherrobbo.dm.entity.manager.EntityManagerProvider;
import com.anotherrobbo.dm.rest.Admin;
import com.anotherrobbo.dm.rest.Index;
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
		bind(Index.class);
		bind(Search.class);
		bind(Overview.class);
		bind(Admin.class);
		bind(Resources.class);
		bind(ServletContext.class).toInstance(context);
		bind(EntityManager.class).toProvider(EntityManagerProvider.class);
	}
}