package com.example.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    @RequestMapping(value = {"/user/dashboard"}, method = RequestMethod.GET)
    public String homePage(){
        return "user/dashboard";
    }

    @RequestMapping(value = {"/user/search"}, method = RequestMethod.GET)
    public String userSearch(){
        return "user/search";
    }

    @RequestMapping(value = {"/user/buy"}, method = RequestMethod.GET)
    public String userBuy(){
        return "user/buy";
    }

    @RequestMapping(value = {"/user/rate"}, method = RequestMethod.GET)
    public String userRate(){
        return "user/rate";
    }
}