package com.abdev.taskmanager;

import com.abdev.taskmanager.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Transactional
class TaskControllerIntegrationTest extends BaseIntegrationTest{
    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldCreateTaskSuccessfully() throws Exception {

        Long userId = TestDataFactory.createTestUser(mockMvc);

        String request = """
                {
                    "title": "Test Task",
                    "description": "Test Descr",
                    "status": "TODO",
                    "dueDate": "2026-06-15T06:04:02.798Z",
                    "assignedUserId": %d
                }
                """.formatted(userId);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void shouldFailValidationWhenUserIdNotFound() throws Exception {

        String request = """
                {
                    "title": "Test Task",
                    "description": "Test Descr",
                    "status": "TODO",
                    "dueDate": "2026-06-15T06:04:02.798Z",
                    "assignedUserId": "0"
                }
                """;

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));

    }

    @Test
    void shouldCreateTaskSuccessfullyAndFetchById() throws Exception {

        Long userId = TestDataFactory.createTestUser(mockMvc);
        Long taskId = TestDataFactory.createTestTask(mockMvc,userId);

        mockMvc.perform(get("/api/v1/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void shouldCreateTaskSuccessfullyAndFetchByStatus() throws Exception {

        Long userId = TestDataFactory.createTestUser(mockMvc);
        TestDataFactory.createMultipleTasks(mockMvc,userId,5);

        mockMvc.perform(get("/api/v1/tasks")
                        .param("status","TODO")
                        .param("page","0")
                        .param("size","5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data[0].title").value("Test Task"));
    }

    @Test
    void shouldReturnPaginatedTasks() throws Exception {

        Long userId = TestDataFactory.createTestUser(mockMvc);
        TestDataFactory.createMultipleTasks(mockMvc,userId,5);

        mockMvc.perform(get("/api/v1/tasks")
                        .param("status","TODO")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.data.length()").value(2));
    }

    @Test
    void shouldReturnEmptyTasks() throws Exception {

        Long userId = TestDataFactory.createTestUser(mockMvc);

        mockMvc.perform(get("/api/v1/tasks")
                        .param("status","TODO")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.data.length()").value(0));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void shouldFailWhenTaskTitleMissing() throws Exception {

        Long userId = TestDataFactory.createTestUser(mockMvc);

        String request = """
            {
                "description": "desc",
                "status": "TODO",
                "assignedUserId": %d
            }
            """.formatted(userId);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }
}
