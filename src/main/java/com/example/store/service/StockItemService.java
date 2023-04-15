package com.example.store.service;

import com.example.store.model.Cart;
import com.example.store.model.CartItem;
import com.example.store.model.StockItem;
import com.example.store.repository.StockItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockItemService {

    @Autowired
    private StockItemRepository stockItemRepository;

    @Transactional
    public List<StockItem> searchStockItems(String category, String manufacturer, String title, String sortBy, String sortDir) {

        Sort sort = Sort.by(sortBy);
        if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Specification<StockItem> spec = Specification.where(null);
        if (category != null && !category.isEmpty()) {
            spec = spec.and(ProductSpecifications.hasCategory(category));
        }
        if (manufacturer != null && !manufacturer.isEmpty()) {
            spec = spec.and(ProductSpecifications.hasManufacturer(manufacturer));
        }
        if (title != null && !title.isEmpty()) {
            spec = spec.and(ProductSpecifications.hasTitle(title));
        }

        return stockItemRepository.findAll(spec, sort);
    }

    public StockItem getStockItemById(Long id) {
        Optional<StockItem> stockItem = stockItemRepository.findById(id);
        return stockItem.orElse(null);
    }

    @Transactional
    public void processOrder(Cart cart) {

        for (CartItem item : cart.getItems()) {
            StockItem stockItem = getStockItemById(item.getProductId());
            if (stockItem == null) {
                throw new IllegalArgumentException("Invalid product ID: " + item.getProductId());
            }
            if (stockItem.getInventoryNum() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient inventory for product ID: " + item.getProductId());
            }
            stockItem.setInventoryNum(stockItem.getInventoryNum() - item.getQuantity());
            stockItemRepository.save(stockItem);
        }
    }

    public List<StockItem> getAllStockItems(String sortBy, String sortDir) {
        Sort sort = Sort.by(sortBy);
        if (sortDir.equals("desc")) {
            sort = sort.descending();
        }
        return stockItemRepository.findAll(sort);
    }

    public void saveStockItem(StockItem stockItem) {
        stockItemRepository.save(stockItem);
    }
}