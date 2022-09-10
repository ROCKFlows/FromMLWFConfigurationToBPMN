package com.ml2wf.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.contract.mapper.IObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApplicationContext {

    private final IObjectMapperFactory objectMapperFactory;

    public ApplicationContext(@Autowired IObjectMapperFactory objectMapperFactory) {
        this.objectMapperFactory = objectMapperFactory;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return objectMapperFactory.createNewObjectMapper();
    }
}
