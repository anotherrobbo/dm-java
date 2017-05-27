package com.anotherrobbo.dm.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import com.google.inject.Module;

@WebListener
public class Listener extends GuiceResteasyBootstrapServletContextListener {
	
	@Override
	protected List<? extends Module> getModules(ServletContext context) {
		return Arrays.asList(new InjectionModule(context));
	}
	
}
