package com.abdev.taskmanager;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Transactional
class TaskControllerIntegrationTest extends BaseIntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    private Long createTestUser() throws Exception {

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

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {

        Long userId = createTestUser();

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

        Long userId = createTestUser();

        String request = """
                {
                    "title": "Test Task",
                    "description": "Test Descr",
                    "status": "TODO",
                    "dueDate": "2026-06-15T06:04:02.798Z",
                    "assignedUserId": %d
                }
                """.formatted(userId);

        MvcResult result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer taskId = JsonPath.read(response, "$.data.id");

        mockMvc.perform(get("/api/v1/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void shouldCreateTaskSuccessfullyAndFetchByStatus() throws Exception {

        Long userId = createTestUser();

        String request = """
                {
                    "title": "Test Task",
                    "description": "Test Descr",
                    "status": "TODO",
                    "dueDate": "2026-06-15T06:04:02.798Z",
                    "assignedUserId": %d
                }
                """.formatted(userId);

        MvcResult result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andReturn();

        mockMvc.perform(get("/api/v1/tasks")
                        .param("status","TODO")
                        .param("page","0")
                        .param("size","5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data[0].title").value("Test Task"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
