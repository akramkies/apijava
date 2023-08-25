package com.quest.etna.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.JwtResponse;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.service.JwtUserDetailsService;

import io.jsonwebtoken.SignatureException;

@Controller
public class AuthenticationController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
	private AuthenticationManager authenticationManager;

    @Autowired
	private JwtUserDetailsService userDetailsService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<com.quest.etna.model.UserDetails> register(@RequestBody User user) {

        if(user.getUsername() == null || user.getPassword() == null){
            return new ResponseEntity<>(new com.quest.etna.model.UserDetails("missing username or password !!", UserRole.ROLE_USER),HttpStatus.BAD_REQUEST);
        }

        User u = new User(user.getUsername(), user.getPassword());
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        com.quest.etna.model.UserDetails details = new  com.quest.etna.model.UserDetails(u.getUsername(), UserRole.valueOf(u.getRole()));
        Optional<User> existUser = userRepository.findByUsername(u.getUsername());

        if(!existUser.isEmpty()) {
               return new ResponseEntity<>(details, HttpStatus.CONFLICT);
        }
        
        userRepository.save(u);
        return new ResponseEntity<>(details,HttpStatus.CREATED);
    } 

	@PostMapping("/authenticate")
    @ResponseBody
	public ResponseEntity<?> authenticate(@RequestBody User user) throws Exception {
        if(user.getUsername() == null || user.getPassword() == null){
            return new ResponseEntity<>("missing username or password !", HttpStatus.BAD_REQUEST);
        }
        else {
            try{
                authenticate(user.getUsername(), user.getPassword());
            }
            catch (Exception e) {
                return new ResponseEntity<>(new JwtResponse("wrong username or password !"), HttpStatus.UNAUTHORIZED);
            }
            final UserDetails userDetails = userDetailsService
            .loadUserByUsername(user.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);
        
            return ResponseEntity.ok(new JwtResponse(token));
        }
	}

    @GetMapping("/me")
    @ResponseBody
    public ResponseEntity<?> me(@RequestHeader(name="Authorization") String token) throws SignatureException {
        String _token = token.split(" ")[1];
        String username = "";
        
        try {
            username = jwtTokenUtil.getUsernameFromToken(_token);
            UserDetails existUser = userDetailsService.loadUserByUsername(username);
            if(existUser.isEnabled()) {
                if(jwtTokenUtil.validateToken(_token, existUser)) {
                    com.quest.etna.model.UserDetails us =  new com.quest.etna.model.UserDetails(existUser.getUsername(), UserRole.ROLE_ADMIN);
                    return  new ResponseEntity<>(us,HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new JwtResponse("JWT invalid ou absent") , HttpStatus.UNAUTHORIZED);
                }
            } 
        } catch (Exception e) {
            return new ResponseEntity<>(new JwtResponse("requete invalide !"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new JwtResponse("requete invalide !") , HttpStatus.BAD_REQUEST);
    }
    

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
