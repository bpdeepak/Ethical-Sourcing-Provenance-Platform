package com.ethicalsource.supplier;

import com.ethicalsource.supplier.domain.Supplier;
import com.ethicalsource.supplier.repository.SupplierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplierServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void testRegisterSupplier() throws Exception {
        Supplier supplier = new Supplier(null, "SUP-001", "Test Supplier", "Berlin", 100, "ACTIVE");

        mockMvc.perform(post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Supplier")));
    }

    @Test
    public void testUpdateTrustScore() throws Exception {
        // Create initial supplier
        Supplier supplier = new Supplier(null, "SUP-002", "Score Test", "London", 50, "ACTIVE");
        repository.save(supplier);

        // Update score +10 (via POST param)
        mockMvc.perform(post("/api/suppliers/SUP-002/trust-score")
                .param("delta", "10"))
                .andExpect(status().isOk());

        // Verify
        mockMvc.perform(get("/api/suppliers/SUP-002/trust-score"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(60)));
    }
}
