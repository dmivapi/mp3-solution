package com.epam.learn.song.mapper;

import com.epam.learn.song.entity.SongEntity;
import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;
import org.mapstruct.Mapper;

@Mapper
public interface SongMapper {

    SongEntity toSongEntity(Song song);

    Song toSong(SongEntity song);
    CreateSongResponse toCreateSongResponse(SongEntity songEntity);
    DeleteSongsResponse toDeleteSongsResponse(Iterable<SongEntity> songEntities);
}
