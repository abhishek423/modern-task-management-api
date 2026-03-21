package com.abdev.taskmanager.util;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestDataFactory {

    public static Long createTestUser(MockMvc mockMvc) throws Exception {

        String request = """
        {
            "name": "Test User",
            "email": "testuser@test.com"
        }
        """;

        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.read(response, "$.data.id");
        return id.longValue();
    }

    public static Long createTestTask(MockMvc mockMvc, Long userId) throws Exception {

        String request = """
        {
            "title": "Test Task",
            "description": "Test Description",
            "status": "TODO",
            "dueDate": "%s",
            "assignedUserId": %d
        }
        """.formatted(LocalDateTime.now().plusDays(1), userId);

        MvcResult result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Number id = JsonPath.read(response, "$.data.id");
        return id.longValue();
    }

    public static void createMultipleTasks(MockMvc mockMvc, Long userId, int count) throws Exception {

        for (int i = 0; i < count; i++) {
            createTestTask(mockMvc, userId);
        }
    }
}
