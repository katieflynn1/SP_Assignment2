package com.example.store.repository;

import com.example.store.model.PurchaseHistory;
import com.example.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long>, JpaSpecificationExecutor<PurchaseHistory> {
    List<PurchaseHistory> findByUser(User user);
}