#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.api.agent.NewRelic;

@Component
public class MDCFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "traceId";
    public static final String USER_ID_KEY = "userId";
    public static final String CLIENT_ID_KEY = "clientId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var principal = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        String clientId = null;
        String userId = null;
        if (principal != null) {
            Jwt jwt = (Jwt)principal.getPrincipal();
            clientId =  ((ArrayList)jwt.getClaim("aud")).get(0).toString();
            userId = jwt.getClaim("custom:customerid");
        }
        var distributedTracePayload = NewRelic.getAgent().getTransaction().createDistributedTracePayload().text();
        Optional<String> json = Optional.ofNullable(distributedTracePayload).filter(Predicate.not(String::isEmpty));

        String traceId = null;
        if (json.isPresent()) {
            JsonNode parent = new ObjectMapper().readTree(distributedTracePayload);
            traceId = parent.get("d").get("tr").asText();
        }

        MDC.put(TRACE_ID_KEY, traceId);
        MDC.put(USER_ID_KEY, userId);
        MDC.put(CLIENT_ID_KEY, clientId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }

    }
}