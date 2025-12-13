package com.ethicalsource.provenance.repository;

import com.ethicalsource.provenance.domain.ProductEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductEventRepository extends JpaRepository<ProductEvent, Long> {
    List<ProductEvent> findByAssetId(String assetId);
    List<ProductEvent> findByAssetIdOrderByTimestampDesc(String assetId);
}
