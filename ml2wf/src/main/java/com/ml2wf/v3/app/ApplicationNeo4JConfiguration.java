package com.ml2wf.v3.app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableNeo4jRepositories(basePackages = "com.ml2wf.v3.app.business.storage.graph.neo4j")
public class ApplicationNeo4JConfiguration {

}
