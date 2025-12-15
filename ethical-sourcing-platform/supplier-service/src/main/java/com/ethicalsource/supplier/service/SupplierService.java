package com.ethicalsource.supplier.service;

import com.ethicalsource.supplier.domain.Supplier;
import com.ethicalsource.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public Supplier registerSupplier(Supplier supplier) {
        Optional<Supplier> existing = repository.findBySupplierId(supplier.getSupplierId());
        if (existing.isPresent()) {
            Supplier s = existing.get();
            s.setName(supplier.getName());
            s.setLocation(supplier.getLocation());
            s.setTrustScore(supplier.getTrustScore());
            s.setStatus(supplier.getStatus());
            return repository.save(s);
        }
        return repository.save(supplier);
    }

    public Optional<Supplier> getSupplier(String supplierId) {
        return repository.findBySupplierId(supplierId);
    }
    
    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }

    public void updateTrustScore(String supplierId, int delta) {
        Optional<Supplier> optionalSupplier = repository.findBySupplierId(supplierId);
        if (optionalSupplier.isPresent()) {
            Supplier supplier = optionalSupplier.get();
            int newScore = supplier.getTrustScore() + delta;
            newScore = Math.max(0, Math.min(100, newScore)); // Clamp between 0 and 100
            supplier.setTrustScore(newScore);
            repository.save(supplier);
        }
    }
}
