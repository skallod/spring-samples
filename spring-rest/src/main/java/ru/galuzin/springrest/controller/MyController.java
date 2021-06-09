package ru.galuzin.springrest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.galuzin.springrest.dto.ApiErrorMessage;
import ru.galuzin.springrest.dto.TestDto;
import ru.galuzin.springrest.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

@Controller
@RequestMapping("/api/v1")
public class MyController {

    private final String setting1;

    private final ObjectMapper mapper;

    public MyController(@Value("${setting1}")String setting1) {
        this.setting1 = setting1;
        this.mapper = new ObjectMapper();
    }

    @PostMapping("/testpost")
    public ResponseEntity<String> testpost(){
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/json");
//        if (!headers.isEmpty()) {
//            throw new Fi
//        }
        if (true) {
            throw new NullPointerException();
        }
        final ArrayList<Object> list = new ArrayList<>();
        return new ResponseEntity<>("good",headers, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getdto")
    @SneakyThrows
    public ResponseEntity<TestDto> getdto() {
        final JsonNode jsonNode = mapper.readTree(new byte[0]);
        return new ResponseEntity<TestDto>(
                new TestDto("test mes 1"),
                HttpStatus.OK);
    }

}
