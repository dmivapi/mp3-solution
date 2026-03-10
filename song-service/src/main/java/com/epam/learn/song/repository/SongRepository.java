package com.epam.learn.song.repository;

import com.epam.learn.song.entity.SongEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends CrudRepository<SongEntity, Long> {
}
