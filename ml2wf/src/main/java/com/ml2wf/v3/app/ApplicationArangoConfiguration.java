package com.ml2wf.v3.app;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableArangoRepositories(basePackages = "com.ml2wf.v3.app.business.storage.graph.arango")
public class ApplicationArangoConfiguration implements ArangoConfiguration {

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
}
