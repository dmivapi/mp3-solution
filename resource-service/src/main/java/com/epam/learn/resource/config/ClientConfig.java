package com.epam.learn.resource.config;

import com.epam.learn.resource.config.properties.SongClientProperties;
import com.epam.learn.song.client.SongsApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties(SongClientProperties.class)
public class ClientConfig {
    @Bean
    public RestClient songsRestClient(SongClientProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }

    @Bean
    public SongsApi songsApi(RestClient songsRestClient) {
        RestClientAdapter restClientAdapter = RestClientAdapter.create(songsRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return factory.createClient(SongsApi.class);
    }
}
