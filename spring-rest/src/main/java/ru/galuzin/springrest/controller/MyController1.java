package ru.galuzin.springrest.controller;

import com.galuzin.autoconf.TestProperties3;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.regex.Pattern;

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

    @Value("${limit.vne.calculation:true}")
    private boolean vneLimitCalculation;

    private static final Pattern ERROR_CODE = Pattern.compile("^\\d+.01$");


    @GetMapping("/subjects/sampleApplication/versions/1")
    @SneakyThrows
    public ResponseEntity<TestDto> getTest() {
//            (HttpServletRequest req) {
//        System.out.println("req = " + req);
        log.debug("versions 1 gettest method called");
        log.info("restartAfterAuthException: {}", properties3.getRestartAfterAuthException());
        log.info("authIntervalRetry: {}", properties3.getAuthIntervalRetry());
        if  (vneLimitCalculation) {
            log.info("calculation: {}", vneLimitCalculation);
        }

        boolean matches = ERROR_CODE.matcher("735.01").matches();
        if (matches) {
            log.info("error code matches");
        }

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
