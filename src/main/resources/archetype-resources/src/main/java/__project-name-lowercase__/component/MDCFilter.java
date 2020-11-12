#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.component;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.api.agent.NewRelic;

@Component
public class MDCFilter extends OncePerRequestFilter {
	
    public static final String TRACE_ID_KEY = "traceId";
    public static final String CLIENT_ID_KEY = "clientId";
    public static final String ISSUED_FOR_KEY = "issuedFor";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	var principal = (KeycloakAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
    	String issuedFor = null;
    	String name = null;
    	if (principal != null) {
    		issuedFor = principal.getAccount().getKeycloakSecurityContext().getToken().getIssuedFor();
    		name = principal.getName();
    	}
        var distributedTracePayload = NewRelic.getAgent().getTransaction().createDistributedTracePayload().text();
        Optional<String> json = Optional.ofNullable(distributedTracePayload).filter(Predicate.not(String::isEmpty));

        String traceId = null;
        if (json.isPresent()) {
            JsonNode parent = new ObjectMapper().readTree(distributedTracePayload);
            traceId = parent.get("d").get("tr").asText();
        }

        MDC.put(TRACE_ID_KEY, traceId);
        MDC.put(CLIENT_ID_KEY, name);
        MDC.put(ISSUED_FOR_KEY, issuedFor);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
        
    }
    
}