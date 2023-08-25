package com.quest.etna.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.Address;
import com.quest.etna.model.ErrorResponse;
import com.quest.etna.model.SuccessResponse;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.service.AddressService;

@Controller
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    UserRepository userRepository;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/address")
    public ResponseEntity<?> getAllAddress(@RequestHeader(name="Authorization") String token) {
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        Iterable<Address> addresses = addressService.getAllAddress();

        ArrayList<Address> _addresses = new ArrayList<Address>();

        if(!existUser.isEmpty()) {
            if(UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN) {
                return new ResponseEntity<>(addresses, HttpStatus.OK);
            }
            else {
                for (Address address : addresses) {
                    if(address.getUserID() == existUser.get().getId()) {
                        _addresses.add(address);
                    }
                }
                return new ResponseEntity<>(_addresses, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error getting list address"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<?> getAddress(@PathVariable("id") int id, @RequestHeader(name="Authorization") String token) {
        Optional<Address> _address = addressService.getAddress(id);

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if(!_address.isEmpty()) {
            if((!existUser.isEmpty() && UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)
            || (!existUser.isEmpty() && UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_USER && existUser.get().getId() == _address.get().getUserID())) {
                return new ResponseEntity<>(_address, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("you don't have permission to access to address with id = " + id), HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(new ErrorResponse("Address with id = " + id + " not found!"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/address")
    public ResponseEntity<?> createAddress(@RequestBody Address address, @RequestHeader(name="Authorization") String token) {
        if(address == null) {
            return new ResponseEntity<>(new ErrorResponse("Address should not be null"), HttpStatus.BAD_REQUEST);
        }

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if(!existUser.isEmpty()) {
            Address updatedAddress = new Address(address.getStreet(), address.getPostalCode(), address.getCity(), address.getCountry(), existUser.get().getId());
            Address _address = addressService.saveAddress(updatedAddress);
            return new ResponseEntity<>(_address, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ErrorResponse("Error creating new address"), HttpStatus.BAD_REQUEST);


    }

    @PutMapping("/address/{id}")
    public ResponseEntity<?> updateAddress(@RequestBody Address newAddress, @PathVariable int id, @RequestHeader(name="Authorization") String token) {
        Optional<Address> _oldAddress = addressService.getAddress(id);

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if(!existUser.isEmpty()) {
            if((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)
            || (UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_USER && existUser.get().getId() == _oldAddress.get().getUserID())) {
                if(!_oldAddress.isEmpty()) {
                    Address _newAddress = new
                        Address(
                            newAddress.getStreet() == null ? _oldAddress.get().getStreet() : newAddress.getStreet(),
                            newAddress.getPostalCode() == null ? _oldAddress.get().getPostalCode() : newAddress.getPostalCode(),
                            newAddress.getCity() == null ? _oldAddress.get().getCity() : newAddress.getCity(),
                            newAddress.getCountry() == null ? _oldAddress.get().getCountry() : newAddress.getCountry(),
                            existUser.get().getId());
                    _newAddress.setId(id);
                    addressService.saveAddress(_newAddress);
                    return new ResponseEntity<>(_newAddress, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new ErrorResponse("Address with id" + id + " not found!"), HttpStatus.NOT_FOUND);
                }
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("You don't have permission to update address with id = " + id), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error updating address with id = " + id), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id, @RequestHeader(name="Authorization") String token) {
        Optional<Address> _address = addressService.getAddress(id);
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if(!existUser.isEmpty()) {
            if((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)
            || (UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_USER && existUser.get().getId() == _address.get().getUserID())) {
                if(addressService.deleteAddress(id)) {
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
}

