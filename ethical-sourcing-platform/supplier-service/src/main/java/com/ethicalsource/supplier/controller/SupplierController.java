package com.ethicalsource.supplier.controller;

import com.ethicalsource.supplier.domain.Supplier;
import com.ethicalsource.supplier.dto.TrustScoreDTO;
import com.ethicalsource.supplier.service.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Supplier> register(@RequestBody Supplier supplier) {
        return ResponseEntity.ok(service.registerSupplier(supplier));
    }
    
    @GetMapping
    public ResponseEntity<List<Supplier>> getAll() {
        return ResponseEntity.ok(service.getAllSuppliers());
    }

    @GetMapping("/{id}/trust-score")
    public ResponseEntity<TrustScoreDTO> getTrustScore(@PathVariable String id) {
        return service.getSupplier(id)
                .map(s -> new TrustScoreDTO(s.getTrustScore(), s.getStatus()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{id}/trust-score")
    public ResponseEntity<Supplier> updateTrustScore(@PathVariable String id, @RequestParam int delta) {
        try {
            Supplier updated = service.updateTrustScore(id, delta);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
