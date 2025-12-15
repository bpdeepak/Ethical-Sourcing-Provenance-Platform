package com.ethicalsource.audit.controller;

import com.ethicalsource.audit.domain.Audit;
import com.ethicalsource.audit.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {
    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Audit> recordAudit(@RequestBody Audit audit) {
        return ResponseEntity.ok(service.recordAudit(audit));
    }

    @GetMapping
    public ResponseEntity<List<Audit>> getAll() {
        return ResponseEntity.ok(service.getAllAudits());
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<Audit>> getBySupplier(@PathVariable String supplierId) {
        return ResponseEntity.ok(service.getAuditsForSupplier(supplierId));
    }
}
