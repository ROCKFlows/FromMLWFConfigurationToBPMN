package com.ml2wf.v3.app;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.v3.xml.XMLObjectMapperFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableArangoRepositories(basePackages = "com.ml2wf.v3.app.business.storage.graph")
public class ApplicationConfiguration implements ArangoConfiguration {

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .host("arango", 8529)
                .user("root")
                .password("ml2wf_password");
    }

    @Override
    public String database() {
        return "ml2wf-database";
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return XMLObjectMapperFactory.getInstance().createNewObjectMapper();
    }
}
