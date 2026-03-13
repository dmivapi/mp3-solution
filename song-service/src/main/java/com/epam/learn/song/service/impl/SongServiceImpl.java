package com.epam.learn.song.service.impl;

import com.epam.learn.song.entity.SongEntity;
import com.epam.learn.song.exception.ParsingIdException;
import com.epam.learn.song.exception.SongAlreadyExistsException;
import com.epam.learn.song.exception.SongNotFoundException;
import com.epam.learn.song.mapper.SongMapper;
import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;
import com.epam.learn.song.repository.SongRepository;
import com.epam.learn.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongMapper songMapper;
    private final SongRepository songRepository;

    @Override
    @Transactional
    public CreateSongResponse createSong(Song song) {
        if (songRepository.existsById(song.getId())) {
            throw new SongAlreadyExistsException(song.getId());
        }

        SongEntity songEntity = songMapper.toSongEntity(song);

        SongEntity persistedEntity = songRepository.save(songEntity);

        return songMapper.toCreateSongResponse(persistedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Song getSong(Long id) {
        return songRepository.findById(id)
                .map(songMapper::toSong)
                .orElseThrow(() -> new SongNotFoundException(id));
    }

    @Override
    @Transactional
    public DeleteSongsResponse removeSongs(String idsCsv) {
        List<Long> requestIds = extractIds(idsCsv);
        Iterable<SongEntity> existingEntities = songRepository.findAllById(requestIds);

        if (existingEntities.iterator().hasNext()) {
            List<Long> existingIds = extractIds(existingEntities);

            songRepository.deleteAllById(existingIds);
        }

        return songMapper.toDeleteSongsResponse(existingEntities);
    }

    private static List<Long> extractIds(Iterable<SongEntity> existingEntities) {
        return StreamSupport.stream(existingEntities.spliterator(), false)
                .map(SongEntity::getId)
                .toList();
    }

    private List<Long> extractIds(String ids) {
        return Arrays.stream(ids.split(","))
                .map(this::getSongId)
                .toList();
    }

    private Long getSongId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException _) {
            throw new ParsingIdException(id);
        }
    }
}
