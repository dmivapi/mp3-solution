package com.epam.learn.resource.mapper;

import com.epam.learn.resource.entity.ResourceEntity;
import com.epam.learn.resource.exception.InvalidMp3Exception;
import com.epam.learn.resource.model.CreateResourceResponse;
import com.epam.learn.resource.model.DeleteResourcesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    @Mapping(target = "id", ignore = true)
    ResourceEntity toResourceEntity(Resource audioData);
    default byte[] toByteArray(Resource audioData) {
        try {
            return audioData.getContentAsByteArray();
        } catch (IOException e) {
            throw new InvalidMp3Exception(e);
        }
    }

    default Resource toResource(ResourceEntity resourceEntity) {
        if (resourceEntity == null || resourceEntity.getAudioData() == null) {
            return null;
        }
        return new ByteArrayResource(resourceEntity.getAudioData());
    }
    CreateResourceResponse toCreateResourceResponse(ResourceEntity resourceEntity);
    default DeleteResourcesResponse toDeleteResourceResponse(Iterable<ResourceEntity> ids) {
        return new DeleteResourcesResponse(toResourceEntityList(ids));
    }

    default List<Long> toResourceEntityList(Iterable<ResourceEntity> resourceEntities) {
        if (resourceEntities == null) {
            return List.of();
        }
        return StreamSupport.stream(resourceEntities.spliterator(), false)
                .map(ResourceEntity::getId)
                .toList();
    }
}
