package com.example.store.service;

import com.example.store.model.StockItem;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<StockItem> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<StockItem> hasManufacturer(String manufacturer) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("manufacturer"), manufacturer);
    }

    public static Specification<StockItem> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }
}