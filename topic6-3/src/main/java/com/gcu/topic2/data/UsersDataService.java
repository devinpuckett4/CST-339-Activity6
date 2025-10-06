package com.gcu.topic2.data;

import org.springframework.stereotype.Service;
import com.gcu.topic2.data.entity.UserEntity;
import com.gcu.topic2.data.repository.UsersRepository;
import java.util.List;

@Service
public class UsersDataService implements UsersDataAccessInterface<UserEntity>, DataAccessInterface<UserEntity> {

    private final UsersRepository repo;

    public UsersDataService(UsersRepository repo) { this.repo = repo; }

    @Override
    public UserEntity findByUsername(String username) {
        return repo.findByUsername(username);
    }

    // The rest are not required by the activity (can be no-ops/minimal)
    @Override public List<UserEntity> findAll() { return List.of(); }
    @Override public UserEntity findById(String id) { return null; }
    @Override public boolean create(UserEntity t) { return false; }
    @Override public boolean update(UserEntity t) { return false; }
    @Override public boolean delete(UserEntity t) { return false; }
}