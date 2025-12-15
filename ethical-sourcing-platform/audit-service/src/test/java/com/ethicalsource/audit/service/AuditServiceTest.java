package com.ethicalsource.audit.service;

import com.ethicalsource.audit.client.SupplierClient;
import com.ethicalsource.audit.domain.Audit;
import com.ethicalsource.audit.repository.AuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {

    @Mock
    private AuditRepository repository;

    @Mock
    private SupplierClient supplierClient;

    @InjectMocks
    private AuditService service;

    @Test
    void recordAudit_shouldSaveAudit_whenSupplierExists() {
        // Arrange
        Audit audit = new Audit("SUP-123", "COMPLIANT", "Files verified");
        when(supplierClient.getTrustScore("SUP-123")).thenReturn(new Object());
        when(repository.save(any(Audit.class))).thenReturn(audit);

        // Act
        Audit saved = service.recordAudit(audit);

        // Assert
        assertNotNull(saved);
        assertEquals("SUP-123", saved.getSupplierId());
        verify(supplierClient).getTrustScore("SUP-123");
        verify(repository).save(audit);
    }

    @Test
    void recordAudit_shouldThrowException_whenSupplierNotFound() {
        // Arrange
        Audit audit = new Audit("INVALID-SUP", "COMPLIANT", "Check");
        when(supplierClient.getTrustScore("INVALID-SUP")).thenThrow(new RuntimeException("Not found"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.recordAudit(audit);
        });

        assertTrue(exception.getMessage().contains("Supplier not found"));
        verify(repository, never()).save(any());
    }
}
