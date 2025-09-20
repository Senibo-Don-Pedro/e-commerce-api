// Create a new file, e.g., config/JacksonConfig.java
package com.senibo.e_commerce_api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                // This feature tells Jackson to use the enum's .toString() method for serialization
                .featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                // This feature tells Jackson to use the enum's .toString() method for deserialization
                .featuresToEnable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }
}
