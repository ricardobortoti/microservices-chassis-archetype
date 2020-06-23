#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.configuration;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.newrelic.NewRelicConfig;
import io.micrometer.newrelic.NewRelicMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NewRelicConfiguration {

    @Value("${symbol_dollar}{management.metrics.export.newrelic.api-key}")
    private  String apiKey;

    @Value("${symbol_dollar}{management.metrics.export.newrelic.account-id}")
    private String accountId;

    @Bean
    public NewRelicConfig getNewRelicConfig(){
        return new NewRelicConfig(){
            @Override
            public String apiKey() {
                return apiKey;
            }

            @Override
            public String accountId() {
                return accountId;
            }

            @Override
            public String get(String s) {
                return null;
            }
        };
    }

    @Bean
    public MeterRegistry getMeterRegistry(NewRelicConfig newRelicConfig){
        return new NewRelicMeterRegistry(newRelicConfig, Clock.SYSTEM);
    }
}