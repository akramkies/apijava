package com.quest.etna.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(origins = "*")
public class DefaultController {

    @GetMapping("/testSuccess")
    @ResponseBody
    public ResponseEntity<String> testSuccess() {
        return new ResponseEntity<>("success",
        HttpStatus.OK);
    }

    @GetMapping("/testNotFound")
    @ResponseBody
    public ResponseEntity<String> testNotFound() {
        return new ResponseEntity<>("not found",
        HttpStatus.NOT_FOUND);
    }

    @GetMapping("/testError")
    @ResponseBody
    public ResponseEntity<String> testError() {
        return new ResponseEntity<>("error",
        HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


