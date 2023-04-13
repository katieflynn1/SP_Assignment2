package com.example.store.service;

import java.util.List;

import com.example.store.model.User;

public interface UserService {
    public void saveUser(User user);
    public List<Object> isUserPresent(User user);
}