package com.epam.learn.resource.kafka;

import com.epam.learn.song.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SongMetadataProducer {

    private final KafkaTemplate<Long, Song> kafkaTemplate;

    @Value("${kafka.topic.song-metadata}")
    private String topicName;

    public void send(Song song) {
        kafkaTemplate.send(topicName, song.getId(), song);
    }
}
