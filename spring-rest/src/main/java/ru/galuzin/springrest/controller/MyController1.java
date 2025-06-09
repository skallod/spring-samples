package ru.galuzin.springrest.controller;

import com.galuzin.autoconf.TestProperties3;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.galuzin.springrest.dto.TestDto;
import ru.galuzin.springrest.service.MyCachedService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(("/api/v1"))
@Slf4j
public class MyController1 {

    private final List<UUID> uuids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

    @Autowired
    private TestProperties3 properties3;

    @Autowired
    private MyCachedService myCachedService;


    @GetMapping("/subjects/sampleApplication/versions/1")
    @SneakyThrows
    public ResponseEntity<TestDto> getTest() {
//            (HttpServletRequest req) {
//        System.out.println("req = " + req);
        log.info("restartAfterAuthException: {}", properties3.getRestartAfterAuthException());
        log.info("authIntervalRetry: {}", properties3.getAuthIntervalRetry());
        return new ResponseEntity<TestDto>(
            new TestDto("test mes 1"),
            HttpStatus.OK);
    }

    @GetMapping("/subjects/sampleApplication/versions/2")
    @SneakyThrows
    public ResponseEntity<Map<UUID, List<TestDto>>> getTest2() {
        return new ResponseEntity<Map<UUID, List<TestDto>>>(
            myCachedService.findAllByEntityIds(uuids),
            HttpStatus.OK);
    }

}
