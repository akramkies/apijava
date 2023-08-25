package com.quest.etna.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<String> register(@RequestBody User user) {
        User u = new User(user.getUsername(),user.getPassword());
        
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exist !", HttpStatus.CONFLICT);
        }
        else {
            User _user = userRepository.save(u);     
            return userRepository.findByUsername(user.getUsername()) != null ? new ResponseEntity<>(new UserDetails(_user.getUsername(), UserRole.valueOf(_user.getRole())).toString(), HttpStatus.CREATED) :
                    new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    @ResponseBody
    public String getUser() {
        return userRepository.findByUsername("akram").toString();
    }
    
}
