package com.todoapp.logintodoapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Register Hibernate6Module to handle Hibernate proxies and lazy loading
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        // Optional: Configure features. For example, to serialize lazy-loaded uninitialized proxies as their ID:
        // hibernate6Module.configure(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
        mapper.registerModule(hibernate6Module);

        // Register JavaTimeModule if you are using Java 8 date/time types like LocalDate
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}