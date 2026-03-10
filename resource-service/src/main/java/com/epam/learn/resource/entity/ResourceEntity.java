package com.epam.learn.resource.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
public class ResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(Types.BINARY)
    @Column(columnDefinition = "bytea")
    private byte[] audioData;
}
