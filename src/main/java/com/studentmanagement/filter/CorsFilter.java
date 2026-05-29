package com.studentmanagement.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Handles CORS preflight (OPTIONS) requests before security filters run,
 * and adds CORS headers to all responses.
 */
@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String ALLOWED_ORIGINS = "http://localhost:5173,http://localhost:4173";

    @Override
    public void filter(ContainerRequestContext request) {
        // Abort preflight OPTIONS immediately with 200 — before security kicks in
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            request.abortWith(Response.ok().build());
        }
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        String origin = request.getHeaderString("Origin");
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            response.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
            response.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        } else if (origin == null) {
            // Non-browser requests (e.g. curl, Swagger UI on same host)
            response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        }
        response.getHeaders().putSingle("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, x-requested-with");
        response.getHeaders().putSingle("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD");
        response.getHeaders().putSingle("Access-Control-Expose-Headers", "Content-Disposition");
        response.getHeaders().putSingle("Access-Control-Max-Age", "86400");
    }
}
