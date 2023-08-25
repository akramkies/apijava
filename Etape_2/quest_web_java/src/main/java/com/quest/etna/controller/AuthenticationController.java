package com.quest.etna.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;

@Controller
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<UserDetails> register(@RequestBody User user) {

        if(user.getUsername() == null || user.getPassword() == null){
            return new ResponseEntity<>(new UserDetails("missing username or password !!", UserRole.ROLE_USER),HttpStatus.BAD_REQUEST);
        }

        User u = new User(user.getUsername(), user.getPassword());
        UserDetails details = new UserDetails(u.getUsername(), UserRole.valueOf(u.getRole()));

        //

        Optional<User> existUser = userRepository.findByUsername(u.getUsername());

        if(!existUser.isEmpty()) {
               return new ResponseEntity<>(details, HttpStatus.CONFLICT);
        }
        
        userRepository.save(u);
        return new ResponseEntity<>(details,HttpStatus.CREATED);

    }
    
}
