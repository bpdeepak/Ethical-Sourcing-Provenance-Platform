package com.ethicalsource.audit.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service")
public interface SupplierClient {
    @GetMapping("/api/suppliers/{id}/trust-score")
    Object getTrustScore(@PathVariable("id") String id);

    @org.springframework.web.bind.annotation.PostMapping("/api/suppliers/{id}/trust-score")
    void updateTrustScore(@PathVariable("id") String id, @org.springframework.web.bind.annotation.RequestParam("delta") int delta);
}
