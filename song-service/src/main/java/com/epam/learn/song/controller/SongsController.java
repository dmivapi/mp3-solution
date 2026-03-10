package com.epam.learn.song.controller;

import com.epam.learn.song.api.SongsApi;
import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;
import com.epam.learn.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SongsController implements SongsApi {

    private final SongService songService;

    @Override
    public ResponseEntity<CreateSongResponse> createSong(Song song) {
        return ResponseEntity.ok(songService.createSong(song));
    }

    @Override
    public ResponseEntity<DeleteSongsResponse> deleteSongs(List<Long> ids) {
        return ResponseEntity.ok(songService.removeSongs(ids));
    }

    @Override
    public ResponseEntity<Song> getSong(Long id) {
        return ResponseEntity.ok(songService.getSong(id));
    }
}
