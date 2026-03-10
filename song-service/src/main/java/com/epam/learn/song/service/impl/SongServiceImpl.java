package com.epam.learn.song.service.impl;

import com.epam.learn.song.entity.SongEntity;
import com.epam.learn.song.mapper.SongMapper;
import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;
import com.epam.learn.song.repository.SongRepository;
import com.epam.learn.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongMapper songMapper;
    private final SongRepository songRepository;

    @Override
    public CreateSongResponse createSong(Song song) {
        SongEntity songEntity = songMapper.toSongEntity(song);

        SongEntity persistedEntity = songRepository.save(songEntity);

        return songMapper.toCreateSongResponse(persistedEntity);
    }

    @Override
    public Song getSong(Long id) {
        return songRepository.findById(id)
                .map(songMapper::toSong)
                .orElseThrow();
    }

    @Override
    public DeleteSongsResponse removeSongs(List<Long> ids) {
        Iterable<SongEntity> existingEntities = songRepository.findAllById(ids);
        List<Long> existingIds = StreamSupport.stream(existingEntities.spliterator(), false)
                        .map(SongEntity::getId)
                        .toList();

        songRepository.deleteAllById(existingIds);

        return songMapper.toDeleteSongsResponse(existingEntities);
    }
}
