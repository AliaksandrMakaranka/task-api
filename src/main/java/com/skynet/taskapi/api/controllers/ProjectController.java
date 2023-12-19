package com.skynet.taskapi.api.controllers;

import com.skynet.taskapi.api.dto.AckDto;
import com.skynet.taskapi.api.dto.ProjectDto;
import com.skynet.taskapi.api.exceptions.BadRequestException;
import com.skynet.taskapi.api.exceptions.NotFoundException;
import com.skynet.taskapi.api.factories.ProjectDtoFactory;
import com.skynet.taskapi.store.entities.ProjectEntity;
import com.skynet.taskapi.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectRepository projectRepository;

    ProjectDtoFactory projectDtoFactory;

    public static final String FETCH_PROJECT = "/api/projects";
    public static final String CREATE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> fetchProject(
            @RequestParam(value = "prefix name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam("project_name") String projectName) {

        if (projectName.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty.");
        }

        findByName(projectName)
                .ifPresent(project -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exist.", projectName));
                });

        ProjectEntity project = projectRepository.saveAndFlush(ProjectEntity.builder().name(projectName).build());
        return projectDtoFactory.makeProjectDto(project);
    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName
    ) {

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

        boolean isCreate = !optionalProjectId.isPresent();

        final ProjectEntity project = optionalProjectId
                .map(this::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        if (isCreate && optionalProjectName.isPresent()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        optionalProjectName
                .ifPresent(projectName -> {

                    projectRepository
                            .findByName(projectName)
                            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(
                                        String.format("Project \"%s\" already exist.", projectName)
                                );
                            });

                    project.setName(projectName);
                });

        final ProjectEntity savedProject = projectRepository.saveAndFlush(project);


        return projectDtoFactory.makeProjectDto(savedProject);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editPatch(@PathVariable("project_id") Long projectId, @RequestParam("project_name") String projectName) {

        if (projectName.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty.");
        }

        ProjectEntity project = getProjectOrThrowException(projectId);


        findByName(projectName)
                .filter(anotherProject -> !Objects.equals(anotherProject.getId(), projectId))
                .ifPresent(anotherProject -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exist.", projectName));
                });

        project.setName(projectName);

        project = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }


    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId) {

        getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);

    }

    private Optional<ProjectEntity> findByName(String name) {
        return projectRepository
                .findByName(name);
    }

    private ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Project with \"%s\" doesn't exist.",
                                        projectId
                                )
                        )
                );
    }

}
