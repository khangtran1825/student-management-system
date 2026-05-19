package com.studentmanagement.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Provider
@ApplicationScoped
public class RateLimitFilter implements ContainerRequestFilter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        // Allows 100 requests per minute per IP
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String ip = requestContext.getUriInfo().getBaseUri().getHost();
        // In a real proxy scenario, you'd check X-Forwarded-For header
        String clientIp = requestContext.getHeaderString("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = ip; // Default to host if no header
        }
        
        if (clientIp == null) {
            clientIp = "unknown";
        }

        Bucket bucket = resolveBucket(clientIp);
        if (!bucket.tryConsume(1)) {
            requestContext.abortWith(
                    Response.status(Response.Status.TOO_MANY_REQUESTS)
                            .entity("Too many requests from this IP. Please try again later.")
                            .build()
            );
        }
    }
}
