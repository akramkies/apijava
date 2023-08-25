package com.quest.etna.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.ErrorResponse;
import com.quest.etna.model.SuccessResponse;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.service.UserService;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        Optional<User> _user = userService.getUser(id);

        if(!_user.isEmpty()) {
            return new ResponseEntity<>(new UserDetails(_user.get().getUsername(), UserRole.valueOf(_user.get().getRole())), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorResponse(String.format("User with id = %s not found!", id)), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id, @RequestHeader(name="Authorization") String token) {
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if(!existUser.isEmpty()) {
            if((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)
            || (UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_USER && existUser.get().getId() == id)) {
                if(userService.deleteUser(id)) {
                    return new ResponseEntity<>(new SuccessResponse(true), HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.NOT_FOUND);
                }
            }
            else {
                return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable int id, @RequestHeader(name="Authorization") String token) {
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if(!existUser.isEmpty()) {
            if((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)) {
                User _newUser = new
                User(
                    newUser.getUsername() == null ? existUser.get().getUsername() : newUser.getUsername(),
                    newUser.getPassword() == null ? existUser.get().getPassword() : passwordEncoder.encode(newUser.getPassword()),
                    newUser.getRole() == null ? existUser.get().getRole() : newUser.getRole()
                );
                _newUser.setId(id);
                User user = userService.saveUser(_newUser);
                return new ResponseEntity<>(new UserDetails( user.getUsername(), UserRole.valueOf(user.getRole())), HttpStatus.OK);
            }
            else {
                if (existUser.get().getId() == id) {
                    User _newUser = new
                    User(
                        newUser.getUsername() == null ? existUser.get().getUsername() : newUser.getUsername(),
                        existUser.get().getPassword(),
                        existUser.get().getRole()
                    );
                    _newUser.setId(id);
                    User user = userService.saveUser(_newUser);
                    return new ResponseEntity<>(new UserDetails( user.getUsername(), UserRole.valueOf(user.getRole())), HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new ErrorResponse("You don't have permission to update user with id = " + id), HttpStatus.FORBIDDEN);
                }
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error updating user with id = " + id), HttpStatus.BAD_REQUEST);
    }
}

