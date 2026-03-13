package com.epam.learn.resource.controller;

import com.epam.learn.resource.api.ResourcesApi;
import com.epam.learn.resource.model.CreateResourceResponse;
import com.epam.learn.resource.model.DeleteResourcesResponse;
import com.epam.learn.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResourcesController implements ResourcesApi {

    private final ResourceService resourceService;

    @Override
    public ResponseEntity<CreateResourceResponse> createResource(Resource body) {
        return ResponseEntity.ok(resourceService.createResource(body));
    }

    @Override
    public ResponseEntity<Resource> getResource(Long id) {
        return ResponseEntity.ok(resourceService.getResource(id));
    }

    @Override
    public ResponseEntity<DeleteResourcesResponse> deleteResources(String id) {
        return ResponseEntity.ok(resourceService.deleteResources(id));
    }
}
