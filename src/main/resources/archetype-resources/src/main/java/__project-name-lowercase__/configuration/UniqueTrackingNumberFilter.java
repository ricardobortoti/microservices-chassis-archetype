#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.configuration;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.api.agent.NewRelic;

@Component
public class UniqueTrackingNumberFilter extends OncePerRequestFilter {
	
    public static final String TRACE_ID_KEY = "traceId";
    public static final String EMPTY_TRACE_ID = "";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var distributedTracePayload = NewRelic.getAgent().getTransaction().createDistributedTracePayload().text();
        Optional<String> json = Optional.ofNullable(distributedTracePayload).filter(Predicate.not(String::isEmpty));

        String traceId = EMPTY_TRACE_ID;
        if (json.isPresent()) {
            JsonNode parent = new ObjectMapper().readTree(distributedTracePayload);
            traceId = parent.get("d").get("tx").asText();
        }

        MDC.put(TRACE_ID_KEY, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }
    
}