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

    @PostMapping("/add-review")
    public String addReview(@RequestParam("itemId") Long itemId, @RequestParam("rating") int rating,
                            @RequestParam("comment") String comment) {
        // GET STOCK ITEM + USER OBJECTS
        StockItem stockItem = stockItemRepository.findById(itemId).orElse(null);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if (stockItem == null || user == null) {
            return "redirect:/";
        }
        Rate rate = new Rate();
        rate.setStockItem(stockItem);
        rate.setUser(user);
        rate.setRating(rating);
        rate.setComment(comment);

        // SAVE RATE OBJECT TO THE DATABASE
        rateRepository.save(rate);
        return "user/rate-success";
    }
}