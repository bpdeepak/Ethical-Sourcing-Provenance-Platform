package com.ethicalsource.audit;

import com.ethicalsource.audit.domain.Audit;
import com.ethicalsource.audit.repository.AuditRepository;
import com.ethicalsource.audit.client.SupplierClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuditServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierClient supplierClient;

    @Autowired
    private AuditRepository repository;

    @Test
    public void testRecordAudit_Success() throws Exception {
        // Mock Supplier Client
        when(supplierClient.getTrustScore("SUP-001")).thenReturn(new com.ethicalsource.audit.dto.TrustScoreDTO(100, "ACTIVE"));
        doNothing().when(supplierClient).updateTrustScore(anyString(), anyInt());

        String auditJson = """
            {
                "supplierId": "SUP-001",
                "complianceStatus": "COMPLIANT",
                "comments": "Integration Test"
            }
        """;

        mockMvc.perform(post("/api/audits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(auditJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testRecordAudit_SupplierNotFound() throws Exception {
        // Mock Supplier Client throwing Exception
        when(supplierClient.getTrustScore("SUP-INVALID")).thenThrow(new RuntimeException("Not Found"));

        String auditJson = """
            {
                "supplierId": "SUP-INVALID",
                "complianceStatus": "COMPLIANT",
                "comments": "Should Fail"
            }
        """;

        mockMvc.perform(post("/api/audits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(auditJson))
                .andExpect(status().isNotFound());
    }
}
