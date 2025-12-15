package com.ethicalsource.provenance;

import com.ethicalsource.provenance.domain.ProductEvent;
import com.ethicalsource.provenance.repository.ProductEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import com.ethicalsource.provenance.client.SupplierClient;

@SpringBootTest
@AutoConfigureMockMvc
public class ProvenanceServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductEventRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SupplierClient supplierClient;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        // Mock the Feign Client call
        com.ethicalsource.provenance.dto.TrustScoreDTO mockScore = new com.ethicalsource.provenance.dto.TrustScoreDTO(100, "ACTIVE");
        when(supplierClient.getTrustScore(anyString())).thenReturn(mockScore);
    }

    @Test
    public void testRecordEvent() throws Exception {
        ProductEvent event = new ProductEvent();
        event.setAssetId("ITEM-123");
        event.setLocation("Factory A");
        // timestamp set by service
        // supplierId is passed via query param

        mockMvc.perform(post("/api/provenance/events")
                .param("supplierId", "SUP-PROV-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetHistory() throws Exception {
        // Seed data
        ProductEvent event = new ProductEvent();
        event.setAssetId("ITEM-HISTORY");
        event.setLocation("Port B");
        event.setTimestamp(java.time.LocalDateTime.now());
        repository.save(event);

        mockMvc.perform(get("/api/provenance/history/ITEM-HISTORY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
