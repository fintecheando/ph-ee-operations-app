package org.apache.fineract.infrastructure.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.fineract.core.domain.FineractPlatformTenant;
import org.apache.fineract.core.serialization.ToApiJsonSerializer;
import org.apache.fineract.core.service.ThreadLocalContextUtil;
import org.apache.fineract.infrastructure.security.data.PlatformRequestLog;
import org.apache.fineract.infrastructure.security.exception.InvalidTenantIdentiferException;
import org.apache.fineract.infrastructure.security.service.BasicAuthTenantDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// @ConditionalOnProperty("fineract.security.basicauth.enabled")
@Profile("basicauth")
public class TenantAwareBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private static boolean firstRequestProcessed = false;
    private static final Logger LOG = LoggerFactory.getLogger(TenantAwareBasicAuthenticationFilter.class);

    private final String tenantRequestHeader = "Fineract-Platform-TenantId";
    private final boolean exceptionIfHeaderMissing = true;

    @Autowired
    private BasicAuthTenantDetailsService basicAuthTenantDetailsService;

    @Autowired
    private ToApiJsonSerializer<PlatformRequestLog> toApiJsonSerializer;

    public TenantAwareBasicAuthenticationFilter(final AuthenticationManager authenticationManager,
            final AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final StopWatch task = new StopWatch();
        task.start();

        try {

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                // ignore to allow 'preflight' requests from AJAX applications
                // in different origin (domain name)
            } else {

                String tenantIdentifier = request.getHeader(this.tenantRequestHeader);

                if (org.apache.commons.lang3.StringUtils.isBlank(tenantIdentifier)) {
                    tenantIdentifier = request.getParameter("tenantIdentifier");
                }

                if (tenantIdentifier == null && this.exceptionIfHeaderMissing) {
                    throw new InvalidTenantIdentiferException("No tenant identifier found: Add request header of '"
                            + this.tenantRequestHeader + "' or add the parameter 'tenantIdentifier' to query string of request URL.");
                }

                String pathInfo = request.getRequestURI();
                boolean isReportRequest = false;
                if (pathInfo != null && pathInfo.contains("report")) {
                    isReportRequest = true;
                }
                final FineractPlatformTenant tenant = this.basicAuthTenantDetailsService.loadTenantById(tenantIdentifier, isReportRequest);

                ThreadLocalContextUtil.setTenant(tenant);
                String authToken = request.getHeader("Authorization");

                if (authToken != null && authToken.startsWith("Basic ")) {
                    ThreadLocalContextUtil.setAuthToken(authToken.replaceFirst("Basic ", ""));
                }

                if (!firstRequestProcessed) {
                    String baseUrl = request.getRequestURL().toString();
                    if (request.getPathInfo() != null) {
                        baseUrl = request.getRequestURL().toString().replace(request.getPathInfo(), "/");
                    }
                    System.setProperty("baseUrl", baseUrl);

                    TenantAwareBasicAuthenticationFilter.firstRequestProcessed = true;
                }
            }

            super.doFilterInternal(request, response, filterChain);
        } catch (final InvalidTenantIdentiferException e) {
            // deal with exception at low level
            SecurityContextHolder.getContext().setAuthentication(null);

            response.addHeader("WWW-Authenticate", "Basic realm=\"" + "Fineract Platform API" + "\"");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } finally {
            task.stop();
            final PlatformRequestLog log = PlatformRequestLog.from(task, request);
            LOG.debug("{}", this.toApiJsonSerializer.serialize(log));
        }
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
            throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
    }

}
