package com.ml2wf.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.contract.mapper.IObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationContext implements WebMvcConfigurer {

    private final IObjectMapperFactory objectMapperFactory;

    public ApplicationContext(@Autowired IObjectMapperFactory objectMapperFactory) {
        this.objectMapperFactory = objectMapperFactory;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return objectMapperFactory.createNewObjectMapper();
    }
}
