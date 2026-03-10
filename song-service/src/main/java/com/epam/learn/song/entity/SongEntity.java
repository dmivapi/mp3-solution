package com.epam.learn.song.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
public class SongEntity {

    @Id
    private Long id;

    private String name;

    private String artist;

    private String album;

    private String duration;

    private Integer year;
}
