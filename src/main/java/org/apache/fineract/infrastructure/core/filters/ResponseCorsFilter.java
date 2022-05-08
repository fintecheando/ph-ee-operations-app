package org.apache.fineract.infrastructure.core.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Filter that returns a response with headers that allows for Cross-Origin Requests (CORs) to be performed against the
 * platform API.
 */

@Provider
@Component
@Scope("singleton")
public class ResponseCorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response) {

        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        final String reqHead = request.getHeaders().getFirst("Access-Control-Request-Headers");

        if (null != reqHead && StringUtils.hasText(reqHead)) {
            response.getHeaders().add("Access-Control-Allow-Headers", reqHead);
        }
    }
}
