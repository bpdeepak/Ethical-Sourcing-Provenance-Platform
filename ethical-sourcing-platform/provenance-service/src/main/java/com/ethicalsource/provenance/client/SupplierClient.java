package com.ethicalsource.provenance.client;

import com.ethicalsource.provenance.dto.TrustScoreDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service")
public interface SupplierClient {
    @GetMapping("/api/suppliers/{id}/trust-score")
    TrustScoreDTO getTrustScore(@PathVariable("id") String id);
}
