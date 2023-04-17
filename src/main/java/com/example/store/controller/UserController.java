package com.example.store.controller;

import com.example.store.model.StockItem;
import com.example.store.repository.RateRepository;
import com.example.store.repository.StockItemRepository;
import com.example.store.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class UserController {

    private final StockItemRepository stockItemRepository;

    public UserController(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }
    @RequestMapping(value = {"/user/dashboard"}, method = RequestMethod.GET)
    public String homePage(){
        return "user/dashboard";
    }

    @RequestMapping(value = {"/user/search"}, method = RequestMethod.GET)
    public String userSearch(){
        return "user/stockItem-list.html";
    }

    @RequestMapping(value = {"/user/buy"}, method = RequestMethod.GET)
    public String userBuy(){
        return "user/buy";
    }

    @RequestMapping(value = {"/user/rate"}, method = RequestMethod.GET)
    public String userRate(Model model) {
        List<StockItem> stockItemList = stockItemRepository.findAll();
        model.addAttribute("stockItemList", stockItemList);
        return "user/rate";
    }
}