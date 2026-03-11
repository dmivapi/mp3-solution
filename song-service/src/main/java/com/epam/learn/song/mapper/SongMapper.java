package com.epam.learn.song.mapper;

import com.epam.learn.song.entity.SongEntity;
import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring")
public interface SongMapper {

    SongEntity toSongEntity(Song song);

    Song toSong(SongEntity song);
    CreateSongResponse toCreateSongResponse(SongEntity songEntity);
    default DeleteSongsResponse toDeleteSongsResponse(Iterable<SongEntity> songEntities) {
        return new DeleteSongsResponse(toResourceEntityList(songEntities));
    }

    default List<Long> toResourceEntityList(Iterable<SongEntity> songEntities) {
        if (songEntities == null) {
            return List.of();
        }
        return StreamSupport.stream(songEntities.spliterator(), false)
                .map(SongEntity::getId)
                .toList();
    }
}
