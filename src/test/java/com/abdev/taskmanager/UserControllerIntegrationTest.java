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
class UserControllerIntegrationTest extends BaseIntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        String request = """
                {
                    "name": "Abhi",
                    "email": "abhi@test.com"
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("abhi@test.com"));
    }

    @Test
    void shouldFailValidationWhenEmailInvalid() throws Exception {

        String request = """
            {
                "name": "",
                "email": "invalid"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors").exists());
    }

    @Test
    void shouldFailWhenUserAlreadyExists() throws Exception {

        String request = """
            {
                "name": "Abhi",
                "email": "dup@test.com"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict());
    }

    void shouldCreateUserAndFetchById() throws Exception {

        String request = """
            {
                "name": "Abhi",
                "email": "abhi@test.com"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Long userId = JsonPath.read(response, "$.data.id");

        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("abhi@test.com"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

}
