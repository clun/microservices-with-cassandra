package org.ff4j.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Configuration Cassandra Driver <-> Spring Data.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Configuration
public class CassandraConfig {
    
    @Bean
    public CqlSession cqlSession() {
        return CqlSession.builder().build();
    }

}
