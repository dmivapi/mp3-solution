package com.epam.learn.resource.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "song-service.client")
public record SongClientProperties (String baseUrl) {}
