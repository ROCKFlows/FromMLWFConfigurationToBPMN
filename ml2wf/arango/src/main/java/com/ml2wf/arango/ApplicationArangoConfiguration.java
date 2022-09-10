package com.ml2wf.arango;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = "com.ml2wf.arango.storage.repository")
public class ApplicationArangoConfiguration implements ArangoConfiguration {

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .host("0.0.0.0", 8529)
                .user("root")
                .password("ml2wf_password");
    }

    @Override
    public String database() {
        return "ml2wf-database";
    }
}
