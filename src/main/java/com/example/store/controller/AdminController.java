package com.example.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {

    @RequestMapping(value = {"/admin/dashboard"}, method = RequestMethod.GET)
    public String adminHome(){
        return "admin/dashboard";
    }

    @RequestMapping(value = {"/admin/stockMgmt"}, method = RequestMethod.GET)
    public String adminStockMgmt(){
        return "admin/stockMgmt";
    }

    @RequestMapping(value = {"/admin/customerHist"}, method = RequestMethod.GET)
    public String adminCustomerHist(){
        return "admin/customerHist";
    }

    @RequestMapping(value = {"/admin/purchaseSim"}, method = RequestMethod.GET)
    public String adminPurchaseSim(){
        return "admin/purchaseSim";
    }

}