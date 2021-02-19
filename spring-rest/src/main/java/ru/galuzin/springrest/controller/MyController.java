package ru.galuzin.springrest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class MyController {

    private final String setting1;

    public MyController(@Value("${setting1}")String setting1) {
        this.setting1 = setting1;
    }

    @PostMapping("/testpost")
    public ResponseEntity<String> testpost(){
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/json");
        return new ResponseEntity<>("good",headers, HttpStatus.ACCEPTED);
    }

}
