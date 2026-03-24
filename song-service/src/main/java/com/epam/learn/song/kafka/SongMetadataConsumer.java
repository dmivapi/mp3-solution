package com.epam.learn.song.kafka;

import com.epam.learn.song.exception.SongAlreadyExistsException;
import com.epam.learn.song.model.Song;
import com.epam.learn.song.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SongMetadataConsumer {

    private final SongService songService;

    @KafkaListener(topics = "${kafka.topic.song-metadata}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Song song) {
        log.info("Received song metadata for resource ID: {}", song.getId());
        try {
            songService.createSong(song);
        } catch (SongAlreadyExistsException _) {
            log.warn("Song metadata already exists for resource ID: {}, skipping", song.getId());
        }
    }
}
