package com.quest.etna.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.Catalog;
import com.quest.etna.model.ErrorResponse;
import com.quest.etna.model.SuccessResponse;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.service.CatalogService;

@Controller
@RequestMapping(value = "/catalog")
@CrossOrigin(origins = "*")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> getCatalogs() {
        return new ResponseEntity<>(catalogService.getCatalogs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCatalog(@PathVariable("id") String id) {
        Optional<Catalog> catalog = catalogService.getCatalog(id);

        if (!catalog.isEmpty()) {
            return new ResponseEntity<>(catalog, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorResponse("Command with id = " + id + " not found!"), HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> createAddress(@RequestBody Catalog catalog, @RequestHeader(name="Authorization") String token) {
        if(catalog == null) {
            return new ResponseEntity<>(new ErrorResponse("Catalog should not be null"), HttpStatus.BAD_REQUEST);
        }

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            if (UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN) {
                Catalog updatedCatalog = new Catalog(catalog.getReference(), catalog.getFamily(), catalog.getDesignation(), catalog.getCostPrice(), catalog.getSalePrice(), catalog.getAmount(), catalog.getImgUrl());
                Catalog _catalog = catalogService.saveAddress(updatedCatalog);
                return new ResponseEntity<>(_catalog, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ErrorResponse("You don't have permission to create new catalog"), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error creating new catalog"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCatalog(@RequestBody Catalog newCatalog, @PathVariable String id, @RequestHeader(name="Authorization") String token) {
        Optional<Catalog> _oldCatalog = catalogService.getCatalog(id);

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            if ((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)) {
                if (!_oldCatalog.isEmpty()) {
                    Catalog _newCatalog = new
                        Catalog(
                            _oldCatalog.get().getReference(),
                            newCatalog.getFamily() == null ? _oldCatalog.get().getFamily() : newCatalog.getFamily(),
                            newCatalog.getDesignation() == null ? _oldCatalog.get().getDesignation() : newCatalog.getDesignation(),
                            newCatalog.getCostPrice() == null ? _oldCatalog.get().getCostPrice() : newCatalog.getCostPrice(),
                            newCatalog.getSalePrice() == null ? _oldCatalog.get().getSalePrice() : newCatalog.getSalePrice(),
                            newCatalog.getAmount() == null ? _oldCatalog.get().getAmount() : newCatalog.getAmount(),
                            newCatalog.getImgUrl() == null ? _oldCatalog.get().getImgUrl() : newCatalog.getImgUrl());
                    _newCatalog.setReference(id);
                    catalogService.saveAddress(_newCatalog);
                    return new ResponseEntity<>(_newCatalog, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new ErrorResponse("Catalog with id" + id + " not found!"), HttpStatus.NOT_FOUND);
                }
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("You don't have permission to update catalog with id = " + id), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error updating catalog with id = " + id), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCatalog(@PathVariable String id, @RequestHeader(name="Authorization") String token) {
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            if ((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)) {
                if (catalogService.deleteAddress(id)) {
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
