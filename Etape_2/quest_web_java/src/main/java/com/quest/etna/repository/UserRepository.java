package com.quest.etna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.User;



@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u from User u WHERE username like %:username%")
    public Optional<User> findByUsername(String username);

}
