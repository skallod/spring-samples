package ru.galuzin.springrest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.galuzin.springrest.dto.TestDto;
import ru.galuzin.springrest.dto.TestDtoExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
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
    public ResponseEntity<List<TestDto>> getdto() {
        log.debug("getdto method called");
        final JsonNode jsonNode = mapper.readTree(new byte[0]);
        return new ResponseEntity<List<TestDto>>(
                m1().stream()
                .filter(Objects::nonNull)
                .peek(it -> log.info("response dto {}", it.getClass().getName()))
                .toList(),
                HttpStatus.OK);
    }

    private List<TestDto> m1 () {
        return (List<TestDto>) m2();
    }

    private List<? extends TestDto> m2 () {
        TestDtoExt testDtoExt = new TestDtoExt();
        testDtoExt.setIsVne(true);
        testDtoExt.setMessage("mess1");
        List<TestDtoExt> l = List.of(testDtoExt);
        return l;
    }

}
