package com.epam.learn.resource.service.impl;

import com.epam.learn.resource.entity.ResourceEntity;
import com.epam.learn.resource.exception.ParsingIdException;
import com.epam.learn.resource.exception.ResourceNotFoundException;
import com.epam.learn.resource.mapper.ResourceMapper;
import com.epam.learn.resource.kafka.SongMetadataProducer;
import com.epam.learn.resource.mapper.SongMapper;
import com.epam.learn.resource.model.CreateResourceResponse;
import com.epam.learn.resource.model.DeleteResourcesResponse;
import com.epam.learn.resource.model.Mp3Metadata;
import com.epam.learn.resource.parcer.Mp3MetadataParser;
import com.epam.learn.resource.repository.ResourceRepository;
import com.epam.learn.resource.service.ResourceService;
import com.epam.learn.song.client.SongsApi;
import com.epam.learn.song.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final ResourceRepository resourceRepository;

    private final Mp3MetadataParser mp3MetadataParser;
    private final SongMapper songMapper;
    private final SongsApi songsApi;
    private final SongMetadataProducer songMetadataProducer;

    @Override
    @Transactional
    public CreateResourceResponse createResource(Resource body) {
        ResourceEntity resourceEntity = resourceMapper.toResourceEntity(body);
        Mp3Metadata mp3Metadata = mp3MetadataParser.extractMp3Metadata(resourceEntity.getAudioData());

        ResourceEntity persistedResourceEntity = resourceRepository.save(resourceEntity);

        Song song = songMapper.toSong(persistedResourceEntity.getId(), mp3Metadata);
        songMetadataProducer.send(song);

        return resourceMapper.toCreateResourceResponse(persistedResourceEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource getResource(Long id) {
        return resourceRepository.findById(id)
                .map(resourceMapper::toResource)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    @Transactional
    public DeleteResourcesResponse deleteResources(String idsCsv) {
        List<Long> requestIds = extractIds(idsCsv);
        Iterable<ResourceEntity> existingEntities = resourceRepository.findAllById(requestIds);

        if (existingEntities.iterator().hasNext()) {
            List<Long> existingIds = extractIds(existingEntities);

            songsApi.deleteSongs(joinIds(existingIds));
            resourceRepository.deleteAllById(existingIds);
        }

        return resourceMapper.toDeleteResourceResponse(existingEntities);
    }

    private List<Long> extractIds(Iterable<ResourceEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(ResourceEntity::getId)
                .toList();
    }
    private List<Long> extractIds(String ids) {
        return Arrays.stream(ids.split(","))
                .map(this::getResourceId)
                .toList();
    }

    private String joinIds(List<Long> ids) {
        return ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private Long getResourceId(String id) {
        try {
            return Long.parseLong(id.trim());
        } catch (NumberFormatException _) {
            throw new ParsingIdException(id);
        }
    }
}
