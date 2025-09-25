package com.senibo.e_commerce_api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the Jackson ObjectMapper for JSON serialization and deserialization.
 * This class customizes how enums are handled to make them more human-readable in API responses.
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates a bean to customize the Jackson ObjectMapper.
     * <p>
     * This customizer enables features to read and write enums using their {@code .toString()}
     * representation rather than their default name or ordinal value. This is useful for enums
     * that have a more descriptive string value.
     *
     * @return A Jackson2ObjectMapperBuilderCustomizer instance with enum features enabled.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                // This feature tells Jackson to use the enum's .toString() method for serialization
                .featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                // This feature tells Jackson to use the enum's .toString() method for deserialization
                .featuresToEnable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }
}
