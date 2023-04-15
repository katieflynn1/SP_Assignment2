package com.example.store.repository;

import com.example.store.model.Cart;
import com.example.store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    List<CartItem> findAllByUserId(Long id);
}