package com.ethicalsource.audit.repository;

import com.ethicalsource.audit.domain.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, String> {
    List<Audit> findBySupplierId(String supplierId);
}
