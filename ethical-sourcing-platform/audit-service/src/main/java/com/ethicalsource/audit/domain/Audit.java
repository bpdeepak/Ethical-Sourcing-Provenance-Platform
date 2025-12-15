package com.ethicalsource.audit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String supplierId;
    private String complianceStatus; // COMPLIANT, NON_COMPLIANT
    private String comments;
    private LocalDateTime auditDate;

    public Audit() {
        this.auditDate = LocalDateTime.now();
    }

    public Audit(String supplierId, String complianceStatus, String comments) {
        this.supplierId = supplierId;
        this.complianceStatus = complianceStatus;
        this.comments = comments;
        this.auditDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getComplianceStatus() { return complianceStatus; }
    public void setComplianceStatus(String complianceStatus) { this.complianceStatus = complianceStatus; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getAuditDate() { return auditDate; }
    public void setAuditDate(LocalDateTime auditDate) { this.auditDate = auditDate; }
}
