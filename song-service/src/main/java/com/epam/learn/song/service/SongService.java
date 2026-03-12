package com.epam.learn.song.service;

import com.epam.learn.song.model.CreateSongResponse;
import com.epam.learn.song.model.DeleteSongsResponse;
import com.epam.learn.song.model.Song;

public interface SongService {

    CreateSongResponse createSong(Song song);

    Song getSong(Long id);

    DeleteSongsResponse removeSongs(String ids);
}
