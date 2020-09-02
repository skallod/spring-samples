package ru.galuzin.springrest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class MyController {

    @PostMapping("/testpost")
    public ResponseEntity<String> testpost(){
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/json");
        return new ResponseEntity<>("good",headers, HttpStatus.ACCEPTED);
    }

}
