package ru.galuzin.springrest.controller;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.galuzin.springrest.dto.TestDto;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(("/api/v1"))
public class MyController1 {

    @GetMapping("/subjects/sampleApplication/versions/1")
    @SneakyThrows
    public ResponseEntity<TestDto> getTest(HttpServletRequest req) {
        System.out.println("req = " + req);
        return new ResponseEntity<TestDto>(
            new TestDto("test mes 1"),
            HttpStatus.OK);
    }

}
