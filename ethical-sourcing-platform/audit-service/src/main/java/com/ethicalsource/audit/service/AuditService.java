package com.ethicalsource.audit.service;

import com.ethicalsource.audit.domain.Audit;
import com.ethicalsource.audit.repository.AuditRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
public class AuditService {
    private final AuditRepository repository;
    private final com.ethicalsource.audit.client.SupplierClient supplierClient;

    public AuditService(AuditRepository repository, com.ethicalsource.audit.client.SupplierClient supplierClient) {
        this.repository = repository;
        this.supplierClient = supplierClient;
    }

    public Audit recordAudit(Audit audit) {
        // Verify supplier exists (sync communication)
        try {
            supplierClient.getTrustScore(audit.getSupplierId());
        } catch (Exception e) {
            log.error("Error verifying supplier {}: {}", audit.getSupplierId(), e.getMessage());
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Supplier not found: " + audit.getSupplierId());
        }

        Audit savedAudit = repository.save(audit);

        // Novel Feature: Dynamic Trust Score Adjustment
        try {
            int delta = "COMPLIANT".equalsIgnoreCase(audit.getComplianceStatus()) ? 5 : -10;
            supplierClient.updateTrustScore(audit.getSupplierId(), delta);
            log.info("Updated trust score for {} with delta: {}", audit.getSupplierId(), delta);
        } catch (Exception e) {
             log.warn("Failed to update trust score: {}", e.getMessage());
             // Don't fail the audit recording if score update fails (Resilience)
        }


        return savedAudit;
    }

    public List<Audit> getAuditsForSupplier(String supplierId) {
        return repository.findBySupplierId(supplierId);
    }

    public List<Audit> getAllAudits() {
        return repository.findAll();
    }
}
