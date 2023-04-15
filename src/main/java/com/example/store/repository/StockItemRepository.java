package com.example.store.repository;

import com.example.store.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockItemRepository extends JpaRepository<StockItem, Long>, JpaSpecificationExecutor<StockItem> {
}