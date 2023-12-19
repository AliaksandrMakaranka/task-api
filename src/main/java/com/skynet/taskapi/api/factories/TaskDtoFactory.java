package com.skynet.taskapi.api.factories;

import com.skynet.taskapi.api.dto.TaskDto;
import com.skynet.taskapi.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity entity) {

        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createAt(entity.getCreateAt())
                .description(entity.getDescription())
                .build();
    }
}
