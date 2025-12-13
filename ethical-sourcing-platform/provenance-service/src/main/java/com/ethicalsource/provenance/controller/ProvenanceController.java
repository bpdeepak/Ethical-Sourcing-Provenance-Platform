package com.ethicalsource.provenance.controller;

import com.ethicalsource.provenance.domain.ProductEvent;
import com.ethicalsource.provenance.service.ProvenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/provenance")
public class ProvenanceController {
    private final ProvenanceService service;

    public ProvenanceController(ProvenanceService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public ResponseEntity<ProductEvent> recordEvent(@RequestBody ProductEvent event, @RequestParam String supplierId) {
        return ResponseEntity.ok(service.recordEvent(event, supplierId));
    }

    @GetMapping("/history/{assetId}")
    public ResponseEntity<List<ProductEvent>> getHistory(@PathVariable String assetId) {
        return ResponseEntity.ok(service.getHistory(assetId));
    }
}
