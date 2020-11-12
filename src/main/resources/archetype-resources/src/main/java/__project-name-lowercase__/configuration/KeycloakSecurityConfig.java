#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.configuration;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled=true, jsr250Enabled=true)
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().requireCsrfProtectionMatcher(keycloakCsrfRequestMatcher())
        	.and()
        	.sessionManagement()
	    	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
	    	.and()
	        .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
	        .addFilterBefore(keycloakAuthenticationProcessingFilter(), LogoutFilter.class)	
	        .addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
	        .addFilterAfter(keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class)
	        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
	        .and()	
	        .logout()
	        .addLogoutHandler(keycloakLogoutHandler())
	        .logoutUrl("/sso/logout").permitAll()
        	.and()
	        .authorizeRequests()
	        .anyRequest()
	        .authenticated();
    }
    
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.GET, "/actuator/health", "/actuator/info");
	}    

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
    
}
