package com.ecoreno;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    void shouldRejectProjectWithoutBudget() throws Exception {
        String payload = "{\"name\":\"Projet test liste\",\"owner\":\"Sophie\",\"description\":\"Travaux complémentaires\",\"impact\":\"Très bon\",\"status\":\"PLANNED\"}";
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.budget").value("Le budget est obligatoire"));
    }

    @Test
    void shouldRejectNegativeBudget() throws Exception {
        String payload = "{\"name\":\"Projet test liste\",\"owner\":\"Sophie\",\"budget\": -5,\"description\":\"Travaux complémentaires\",\"impact\":\"Très bon\",\"status\":\"PLANNED\"}";
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.budget").value("Le budget doit être positif"));
    }

    @Test
    void shouldReturn404WhenProjectNotFound() throws Exception {
        mockMvc.perform(get("/api/projects/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Projet introuvable avec l'id 999"));
    }

    @Test
    void shouldGetProjectById() throws Exception {
        String payload = "{\"name\":\"Projet test GET par id\",\"owner\":\"Sophie\",\"budget\":6000,\"description\":\"Travaux complémentaires\",\"impact\":\"Très bon\",\"status\":\"PLANNED\"}";
        MvcResult result = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andReturn();
        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Projet test GET par id"));
    }

    @Test
    void shouldUpdateProject() throws Exception {
        String createdPayload = "{\"name\":\"Projet avant PUT\",\"owner\":\"Sophie\",\"budget\":6000,\"description\":\"Travaux complémentaires\",\"impact\":\"Très bon\",\"status\":\"PLANNED\"}";
        String updatePayload = "{\"name\":\"Projet après PUT\",\"owner\":\"Carl\",\"budget\":8653,\"description\":\"Travaux toitures\",\"impact\":\"Très bon\",\"status\":\"IN_PROGRESS\"}";
        MvcResult result = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createdPayload))
                .andExpect(status().isCreated())
                .andReturn();
        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(put("/api/projects/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Projet après PUT"))
                .andExpect(jsonPath("$.owner").value("Carl"))
                .andExpect(jsonPath("$.budget").value(8653.00))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.description").value("Travaux toitures"));

        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Projet après PUT"));
    }

    @Test
    void shouldReturn404WhenUpdatingUnknownProject() throws Exception {
        String updatePayload = "{\"name\":\"Projet avant PUT\",\"owner\":\"Sophie\",\"budget\":6000,\"description\":\"Travaux complémentaires\",\"impact\":\"Très bon\",\"status\":\"PLANNED\"}";
        mockMvc.perform(put("/api/projects/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Projet introuvable avec l'id 999"));
    }
}
