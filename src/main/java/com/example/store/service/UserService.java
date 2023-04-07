package com.example.store.service;

import java.util.List;

import com.example.calendar.model.User;

public interface UserService {
    public void saveUser(User user);
    public List<Object> isUserPresent(User user);
}