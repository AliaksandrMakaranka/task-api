package com.skynet.taskapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Long projectId;

    @BeforeEach
    void setup() throws Exception {
        // Создаем проект для тестов TaskState
        String response = mockMvc.perform(put("/api/projects")
                .param("project_name", "ProjectForTaskState"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        projectId = com.jayway.jsonpath.JsonPath.read(response, "$.id");
    }

    @Test
    void getTaskStatesReturnsOk() throws Exception {
        mockMvc.perform(get("/api/projects/" + projectId + "/task-states"))
                .andExpect(status().isOk());
    }

    @Test
    void createTaskStateReturnsTaskState() throws Exception {
        mockMvc.perform(post("/api/projects/" + projectId + "/task-states")
                .param("task_state_name", "ToDo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ToDo"));
    }

    @Test
    void createTaskStateWithoutNameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/projects/" + projectId + "/task-states"))
                .andExpect(status().isBadRequest());
    }
} 