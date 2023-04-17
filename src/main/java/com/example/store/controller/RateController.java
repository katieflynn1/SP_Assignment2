package com.example.store.controller;

import com.example.store.model.Rate;
import com.example.store.model.StockItem;
import com.example.store.model.User;
import com.example.store.repository.RateRepository;
import com.example.store.repository.StockItemRepository;
import com.example.store.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rate")
public class RateController {

    private final RateRepository rateRepository;
    private final StockItemRepository stockItemRepository;
    private final UserRepository userRepository;

    public RateController(RateRepository rateRepository, StockItemRepository stockItemRepository, UserRepository userRepository) {
        this.rateRepository = rateRepository;
        this.stockItemRepository = stockItemRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/rate-form/{stockItemId}")
    public String showRateForm(@PathVariable Long stockItemId, Model model) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stock item id"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Rate rate = new Rate();
        rate.setStockItem(stockItem);
        rate.setUser(user);
        model.addAttribute("rate", rate);
        return "user/rate-form";
    }

    @PostMapping("/add-review")
    public String addReview(@ModelAttribute Rate rate, @RequestParam("stockItemId") Long itemId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        rate.setUser(user);
        StockItem item = stockItemRepository.findById(itemId).orElseThrow();
        rate.setStockItem(item);
        rateRepository.save(rate);
        return "redirect:/products/rate";
    }

    @GetMapping("/{stockItemId}/success")
    public String showRateSuccessPage(@PathVariable Long stockItemId, Model model) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stock item id"));
        model.addAttribute("stockItem", stockItem);
        return "user/rate-success";
    }
}