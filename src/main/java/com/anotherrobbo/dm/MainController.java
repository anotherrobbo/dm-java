package com.anotherrobbo.dm;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;

import com.anotherrobbo.dm.web.Listener;
import com.anotherrobbo.dm.web.WebConfig;

/**
 * This is the MainController for the SpringBoot application
 */
public class MainController {
    
    private static final String RESTEASY_SERVLET_NAME = "destingmatches";
    
    public static void main(String[] args) throws Exception {
        String contextPath = "/";
        String appBase = ".";
        Tomcat tomcat = new Tomcat();
        tomcat.setAddDefaultWebXmlToWebapp(false);

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.valueOf(webPort));
        tomcat.getConnector();
        
        Context ctx = tomcat.addContext(contextPath, new File(appBase).getAbsolutePath());
        ctx.addApplicationListener(Listener.class.getName());
        Tomcat.addServlet(ctx, RESTEASY_SERVLET_NAME, new HttpServletDispatcher());
        ctx.addParameter("javax.ws.rs.Application", WebConfig.class.getName());
        ctx.addServletMappingDecoded("/*", RESTEASY_SERVLET_NAME);

//        StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath, new File(appBase).getAbsolutePath());
//        System.out.println("configuring app with basedir: " + new File(appBase).getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("target/classes/WEB-INF");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
    
}
