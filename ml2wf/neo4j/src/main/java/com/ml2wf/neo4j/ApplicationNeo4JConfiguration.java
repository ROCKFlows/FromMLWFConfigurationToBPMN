package com.ml2wf.neo4j;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableNeo4jRepositories(basePackages = "com.ml2wf.neo4j.storage.repository")
public class ApplicationNeo4JConfiguration {

}
