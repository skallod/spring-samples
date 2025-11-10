package ru.galuzin.springrest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.galuzin.springrest.dto.TestDto;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("${api.path}")
public class MyController {

    private final String setting1;

    private final ObjectMapper mapper;

    private final Pattern pattern;

    public MyController(@Value("${setting1}")String setting1,
                        @Value("${api.pattern}") String patternStr) {
        this.setting1 = setting1;
        this.mapper = new ObjectMapper();
        pattern = Pattern.compile(patternStr);
    }

    /*@PostConstruct
    public void init() {
        pattern = Pattern.compile(patternStr);
        if (pattern.matcher("+79995556644").find()) {
            log.info("!!!! Matches !!!!");
        }
    }*/

    @PostMapping("/testpost")
    public ResponseEntity<String> testpost(){
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/json");
//        if (true) {
//            throw new NullPointerException();
//        }
        final ArrayList<Object> list = new ArrayList<>();
        return new ResponseEntity<>("good",headers, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getdto")
    @SneakyThrows
    public ResponseEntity<TestDto> getdto() {
        log.debug("getdto method called");
        final JsonNode jsonNode = mapper.readTree(new byte[0]);
        return new ResponseEntity<TestDto>(
                new TestDto("test mes 1"),
                HttpStatus.OK);
    }

}
