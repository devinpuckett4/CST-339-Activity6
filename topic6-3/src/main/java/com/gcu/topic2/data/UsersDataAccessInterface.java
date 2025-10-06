package com.gcu.topic2.data;

public interface UsersDataAccessInterface<T> {
    T findByUsername(String username);
}