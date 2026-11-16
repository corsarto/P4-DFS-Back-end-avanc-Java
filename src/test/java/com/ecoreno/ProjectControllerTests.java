package com.ecoreno;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListProjects() throws Exception {
        String payload = "{\"name\":\"Projet test liste\",\"owner\":\"Sophie\",\"budget\":6000,\"description\":\"Travaux complémentaires\",\"impact\":\"Très bon\",\"status\":\"PLANNED\"}";
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].name", hasItem("Projet test liste")));
    }

    @Test
    void shouldCreateProject() throws Exception {
        String payload = "{\"name\":\"Isolation double vitrage\",\"owner\":\"Sophie\",\"budget\":6000,\"description\":\"Travaux complémentaires\",\"impact\":\"Très bon\",\"status\":\"PLANNED\"}";

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Isolation double vitrage"));
    }
}
