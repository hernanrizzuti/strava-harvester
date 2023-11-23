package com.rizzutih.stravaharvester.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.strava.api")
public class ApplicationConfigProperties {

    private String uri;

    private Endpoints endpoints;
}
