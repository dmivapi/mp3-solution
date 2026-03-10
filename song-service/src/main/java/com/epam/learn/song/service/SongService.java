package com.epam.learn.song.service;

import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;

import java.util.List;

public interface SongService {

    CreateSongResponse createSong(Song song);

    Song getSong(Long id);

    DeleteSongsResponse removeSongs(List<Long> ids);
}
