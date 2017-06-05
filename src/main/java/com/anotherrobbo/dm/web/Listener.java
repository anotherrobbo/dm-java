package com.anotherrobbo.dm.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import com.anotherrobbo.dm.entity.manager.EntityManagerProvider;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

@WebListener
public class Listener extends GuiceResteasyBootstrapServletContextListener {
	
	@Override
	protected List<? extends Module> getModules(ServletContext context) {
		return Arrays.asList(new InjectionModule(context),
							 new JpaPersistModule("dm").properties(EntityManagerProvider.getPropertyOverrides()));
	}
	
	@Override
	protected void withInjector(Injector injector) {
		injector.getInstance(PersistService.class).start();
	}
	
}
