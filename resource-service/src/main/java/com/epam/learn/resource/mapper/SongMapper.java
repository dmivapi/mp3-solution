package com.epam.learn.resource.mapper;

import com.epam.learn.resource.model.Mp3Metadata;
import com.epam.learn.song.model.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {

    Song toSong(Long id, Mp3Metadata mp3Metadata);
}
