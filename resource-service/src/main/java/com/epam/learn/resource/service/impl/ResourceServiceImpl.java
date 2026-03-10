package com.epam.learn.resource.service.impl;

import com.epam.learn.resource.entity.ResourceEntity;
import com.epam.learn.resource.mapper.ResourceMapper;
import com.epam.learn.resource.model.CreateResourceResponse;
import com.epam.learn.resource.model.DeleteResourcesResponse;
import com.epam.learn.resource.repository.ResourceRepository;
import com.epam.learn.resource.service.ResourceService;
import com.epam.learn.song.client.SongsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final ResourceRepository resourceRepository;
    private final SongsApi songsApi;

    @Override
    @Transactional
    public CreateResourceResponse createResource(Resource body) {
        ResourceEntity resourceEntity = resourceMapper.toResourceEntity(body);
        ResourceEntity persistedResourceEntity = resourceRepository.save(resourceEntity);

        return resourceMapper.toCreateResourceResponse(persistedResourceEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource getResource(Long id) {
        return resourceRepository.findById(id)
                .map(resourceMapper::toResource)
                .orElseThrow();
    }

    @Override
    @Transactional
    public DeleteResourcesResponse deleteResources(List<Long> ids) {
        Iterable<ResourceEntity> existingEntities = resourceRepository.findAllById(ids);
        List<Long> existingIds = StreamSupport.stream(existingEntities.spliterator(), false)
                .map(ResourceEntity::getId)
                .toList();

        songsApi.deleteSongs(existingIds);
        resourceRepository.deleteAllById(existingIds);

        return resourceMapper.toDeleteResourceResponse(existingEntities);
    }
}
