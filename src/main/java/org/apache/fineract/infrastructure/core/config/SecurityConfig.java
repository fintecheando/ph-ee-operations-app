package org.apache.fineract.infrastructure.core.config;

import org.apache.fineract.infrastructure.security.filter.TenantAwareBasicAuthenticationFilter;
import org.apache.fineract.infrastructure.security.service.TenantAwareJpaPlatformUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@ConditionalOnProperty("fineract.security.basicauth.enabled")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TenantAwareJpaPlatformUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http //
                .csrf().disable() // NOSONAR only creating a service that is used by non-browser clients
                .antMatcher("/api/**").authorizeRequests() //
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() //
                .antMatchers(HttpMethod.POST, "/api/*/echo").permitAll() //
                .antMatchers("/actuator/**").permitAll() //
                .antMatchers("/api/*/twofactor").fullyAuthenticated() //
                .antMatchers("/api/**").access("isFullyAuthenticated()").and() //
                .httpBasic() //
                .authenticationEntryPoint(basicAuthenticationEntryPoint()) //
                .and() //
                .sessionManagement() //
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
                .and() //
                .addFilterAfter(tenantAwareBasicAuthenticationFilter(), SecurityContextPersistenceFilter.class); //

        /*
        if (serverProperties.getSsl().isEnabled()) {
            http.requiresChannel(channel -> channel.antMatchers("/api/**").requiresSecure());
        }
        */
    }

    @Bean
    public TenantAwareBasicAuthenticationFilter tenantAwareBasicAuthenticationFilter() throws Exception {
        return new TenantAwareBasicAuthenticationFilter(authenticationManagerBean(), basicAuthenticationEntryPoint());
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("Fineract Platform API");
        return basicAuthenticationEntryPoint;
    }

    @Bean(name = "customAuthenticationProvider")
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
        auth.eraseCredentials(false);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterRegistrationBean<TenantAwareBasicAuthenticationFilter> tenantAwareBasicAuthenticationFilterRegistration()
            throws Exception {
        FilterRegistrationBean<TenantAwareBasicAuthenticationFilter> registration = new FilterRegistrationBean<TenantAwareBasicAuthenticationFilter>(
                tenantAwareBasicAuthenticationFilter());
        registration.setEnabled(false);
        return registration;
    }
}
