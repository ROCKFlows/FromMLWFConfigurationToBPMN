package com.ml2wf.v3.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.v3.app.xml.XMLObjectMapperFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ApplicationContext {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return XMLObjectMapperFactory.getInstance().createNewObjectMapper();
    }

}
