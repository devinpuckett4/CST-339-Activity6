package com.gcu.topic2.model;

import org.springframework.stereotype.Service;

@Service
public class UsersDataService implements UsersDataAccessInterface<UserEntity> {

    private final UsersRepository repo;

    public UsersDataService(UsersRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return repo.findByUsername(username);
    }

    // (Optional) stubs for create/update/delete can go here if needed by your course
}