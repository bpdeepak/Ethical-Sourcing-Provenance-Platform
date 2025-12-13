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
        return repository.save(supplier);
    }

    public Optional<Supplier> getSupplier(String supplierId) {
        return repository.findBySupplierId(supplierId);
    }
    
    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }
}
