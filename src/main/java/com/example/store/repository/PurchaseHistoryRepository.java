package com.example.store.repository;

import com.example.store.model.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long>, JpaSpecificationExecutor<PurchaseHistory> {
}