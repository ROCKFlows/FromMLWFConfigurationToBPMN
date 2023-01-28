package com.ml2wf.app;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.ml2wf.contract.mapper.IObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
@EnableAspectJAutoProxy
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
    public HttpMessageConverters customConverters() {
        objectMapperFactory.createNewObjectMapper();
        final AbstractJackson2HttpMessageConverter xmlConverter = new MappingJackson2HttpMessageConverter(
                objectMapperFactory.createNewObjectMapper()
        );
        xmlConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_XML));
        final AbstractJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(
                new JsonMapper()
        );
        jsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpMessageConverters(xmlConverter, jsonConverter);
    }
}
