package com.skynet.taskapi.api.controllers;

import com.skynet.taskapi.api.dto.ProjectDto;
import com.skynet.taskapi.api.exceptions.BadRequestException;
import com.skynet.taskapi.api.factories.ProjectDtoFactory;
import com.skynet.taskapi.store.entities.ProjectEntity;
import com.skynet.taskapi.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectRepository projectRepository;

    ProjectDtoFactory projectDtoFactory;

    public static final String CREATE_PROJECT = "/api/projects";

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {

        projectRepository
                .findByName(name)
                .ifPresent(projectEntity -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exist.", name));
                });

        //TODO: uncommit and insert entity in method
        //https://www.youtube.com/watch?v=gcT5RPfa10U&list=PLb9LG4UcPZxB8QHwxrGUb8wdJmUMd8KOD
        // 2.33.06 time

        return null;
    }
}

