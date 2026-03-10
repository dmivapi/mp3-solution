package com.epam.learn.resource.service;

import com.epam.learn.resource.model.CreateResourceResponse;
import com.epam.learn.resource.model.DeleteResourcesResponse;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ResourceService {

    CreateResourceResponse createResource(Resource body);

    Resource getResource(Long id);

    DeleteResourcesResponse deleteResources(List<Long> ids);
}
