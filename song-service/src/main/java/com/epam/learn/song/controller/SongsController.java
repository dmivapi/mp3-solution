package com.epam.learn.song.controller;

import com.epam.learn.song.api.SongsApi;
import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class SongsController implements SongsApi {
    @Override
    public ResponseEntity<CreateSongResponse> createSong(Song song) {
        return null;
    }

    @Override
    public ResponseEntity<DeleteSongsResponse> deleteSongs(List<Long> ids) {
        return null;
    }

    @Override
    public ResponseEntity<Song> getSong(Long id) {
        return null;
    }
}
