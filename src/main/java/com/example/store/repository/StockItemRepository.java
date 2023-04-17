package com.example.store.repository;

import com.example.store.model.StockItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItem, Long>, JpaSpecificationExecutor<StockItem> {
    List<StockItem> findAll(Specification<StockItem> spec, Sort sort);
    Page<StockItem> findAll(Specification<StockItem> spec, Pageable pageable);

    List<StockItem> findByCategoryContainingIgnoreCase(String category);

    List<StockItem> findByManufacturerContainingIgnoreCase(String manufacturer);

    List<StockItem> findByTitleContainingIgnoreCase(String title);
}