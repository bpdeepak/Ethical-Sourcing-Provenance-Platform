package com.ethicalsource.provenance.service;

import com.ethicalsource.provenance.client.SupplierClient;
import com.ethicalsource.provenance.domain.ProductEvent;
import com.ethicalsource.provenance.dto.TrustScoreDTO;
import com.ethicalsource.provenance.repository.ProductEventRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProvenanceService {
    private final ProductEventRepository repository;
    private final SupplierClient supplierClient;

    public ProvenanceService(ProductEventRepository repository, SupplierClient supplierClient) {
        this.repository = repository;
        this.supplierClient = supplierClient;
    }

    public ProductEvent recordEvent(ProductEvent event, String supplierId) {
        // Validate supplier
        TrustScoreDTO trustScore = supplierClient.getTrustScore(supplierId);
        if ("BANNED".equalsIgnoreCase(trustScore.getStatus())) {
            throw new RuntimeException("Shipment denied: Supplier is BANNED");
        }
        
        event.setTimestamp(LocalDateTime.now());
        return repository.save(event);
    }

    public List<ProductEvent> getHistory(String assetId) {
        return repository.findByAssetIdOrderByTimestampDesc(assetId);
    }
}
