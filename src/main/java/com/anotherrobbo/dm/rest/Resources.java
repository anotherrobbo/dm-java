package com.anotherrobbo.dm.rest;

import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/resources")
public class Resources {
	
	private static final String RESOURCE_PREFIX = "WEB-INF/resources/";
	
	@Inject
	private ServletContext servletContext;

	@GET
    @Path("/{path:.*}")
    public Response getResource(@PathParam("path") String path) {
        try {
            InputStream resource = servletContext.getResourceAsStream(RESOURCE_PREFIX + path);
            if (resource != null) {
                return Response.ok(resource)/*.type(MimeTypeUtil.getContentType(path)).cacheControl(CacheControlUtil.getDefaultCacheControl())*/.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
//            ServicesLogger.LOGGER.failedToGetThemeRequest(e);
            return Response.serverError().build();
        }
    }
	
}
