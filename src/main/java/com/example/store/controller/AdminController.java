package com.example.store.controller;

import com.example.store.model.PurchaseHistory;
import com.example.store.model.User;
import com.example.store.model.UserPurchase;
import com.example.store.repository.PurchaseHistoryRepository;
import com.example.store.repository.StockItemRepository;
import com.example.store.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public AdminController(UserRepository userRepository, PurchaseHistoryRepository purchaseHistoryRepository) {
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    @RequestMapping(value = {"/admin/dashboard"}, method = RequestMethod.GET)
    public String adminHome() {
        return "admin/dashboard";
    }

    @RequestMapping(value = {"/admin/stockMgmt"}, method = RequestMethod.GET)
    public String adminStockMgmt() {
        return "admin/stockMgmt";
    }

    @RequestMapping(value = {"/admin/customerHist"}, method = RequestMethod.GET)
    public String adminCustomerHist(Model model) {

        // GET ALL USERS
        List<User> users = userRepository.findAll();

        // CREATE LIST TO STORE USERPURCHASE OBJECTS
        List<UserPurchase> userPurchases = new ArrayList<>();

        for (User user : users) {
            List<PurchaseHistory> purchaseHistories = purchaseHistoryRepository.findByUser(user);

            // CREATE NEW USERPURCHASE OBJECT FOR EACH PURCHASE HISTORY, ADD TO LIST
            for (PurchaseHistory purchaseHistory : purchaseHistories) {
                UserPurchase userPurchase = new UserPurchase(user.getEmail(), purchaseHistory.getTitle(),
                        purchaseHistory.getQuantity(), purchaseHistory.getPrice(),
                        purchaseHistory.getTotalAmount());
                userPurchases.add(userPurchase);
            }
        }
        model.addAttribute("userPurchases", userPurchases);

        return "admin/customerHist";
    }
}