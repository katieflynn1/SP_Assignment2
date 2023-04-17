package com.example.store.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchase_history")
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_history_id")
    private List<CartItem> purchaseItems = new ArrayList<>();

    @Column(name = "title")
    private String title;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "total_amount")
    private double totalAmount;

    public PurchaseHistory(){}
    public PurchaseHistory(String title, int quantity, double price, double totalAmount, User user) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.user = user;
    }

    // GETTERS + SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getPurchaseItems() {
        return purchaseItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setTitle(String title) {this.title = title;}

    public void setPrice(double price) {this.price = price;}
}