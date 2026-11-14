package com.employe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRootEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("Employee Management REST API")))
                .andExpect(jsonPath("$.endpoints", notNullValue()));
    }

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$[0].firstName", notNullValue()));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.department", is("Engineering")));
    }

    @Test
    void testGetEmployeeNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEmployee() throws Exception {
        String employeeJson = "{" +
                "\"firstName\": \"Clark\"," +
                "\"lastName\": \"Kent\"," +
                "\"email\": \"clark.kent@dailyplanet.com\"," +
                "\"department\": \"Editorial\"," +
                "\"role\": \"Reporter\"," +
                "\"salary\": 65000.0" +
                "}";

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Clark")))
                .andExpect(jsonPath("$.salary", is(65000.0)));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        String updateJson = "{" +
                "\"firstName\": \"John\"," +
                "\"lastName\": \"Doe\"," +
                "\"email\": \"john.updated@example.com\"," +
                "\"department\": \"Engineering\"," +
                "\"role\": \"Lead Architect\"," +
                "\"salary\": 120000.0" +
                "}";

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("Lead Architect")))
                .andExpect(jsonPath("$.salary", is(120000.0)));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/employees/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterByDepartment() throws Exception {
        mockMvc.perform(get("/api/employees/department/Engineering"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].department", equalToIgnoringCase("Engineering")));
    }
}
