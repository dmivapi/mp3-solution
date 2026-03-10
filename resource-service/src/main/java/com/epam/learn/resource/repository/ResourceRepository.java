package com.epam.learn.resource.repository;

import com.epam.learn.resource.entity.ResourceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends CrudRepository<ResourceEntity, Long> {
}
