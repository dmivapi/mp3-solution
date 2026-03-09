package com.epam.learn.resource.controller;

import com.epam.learn.resource.api.ResourcesApi;
import com.epam.learn.resource.model.CreateResourceResponse;
import com.epam.learn.resource.model.DeleteResourcesResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResourcesController implements ResourcesApi {
    @Override
    public ResponseEntity<CreateResourceResponse> createResource(Resource body) {
        return null;
    }

    @Override
    public ResponseEntity<DeleteResourcesResponse> deleteResources(List<Integer> id) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> getResource(String id) {
        return null;
    }
}
