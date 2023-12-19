package com.skynet.taskapi.api.factories;

import com.skynet.taskapi.api.dto.ProjectDto;
import com.skynet.taskapi.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity entity) {

        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreateAt())
                .updatedAt(entity.getUpdateAt())
                .build();
    }
}
