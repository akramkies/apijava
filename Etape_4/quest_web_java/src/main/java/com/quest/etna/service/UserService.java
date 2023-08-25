package com.quest.etna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetail;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<UserDetail> getAllUsers() {
        List<User> users = new ArrayList<User>();
        List<UserDetail> result = new ArrayList<UserDetail>();
        userRepository.findAll().forEach(users::add);

        for(int i = 0 ; i< users.size() ; i++) {
            result.add(new UserDetail(users.get(i).getId(), users.get(i).getUsername(), UserRole.valueOf(users.get(i).getRole())));
        }
        return result;
    }

    public Optional<User> getUser(int id) {
        return userRepository.findById(id);
    }

    public boolean deleteUser( int id) {
        Optional<User> _user = userRepository.findById(id);
        if(!_user.isEmpty()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User saveUser(User user) {
        User _user = userRepository.save(user);
        return _user;
    }

}

