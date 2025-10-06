package com.gcu.topic2.model;

public interface UsersDataAccessInterface<T> {
    T findByUsername(String username);
}