package ru.galuzin.springrest.controller;

import com.galuzin.autoconf.TestProperties3;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.galuzin.springrest.dto.TestDto;
import ru.galuzin.springrest.service.MyCachedService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

//import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
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
    public ResponseEntity<Map<UUID, List<TestDto>>> getTest2(
        @Parameter(description = "Номер договора")
        @RequestParam(required = false) String contractNumber,
        @Parameter(description = "Мдм код")
        @RequestParam(required = false) Long mdmCode
    ) {

        String html = """
            &lt;/p&gt; &lt;/p&gt; Text1 &lt;br&gt; Text2
            <p style="text-align: jSome text
            </p>
            <p style="text-align: j&nbsp;<span></span>Test4.</p>
            """;

        // 1. Convert entities back to raw HTML tags ("Hello <p>World</p>")
        String rawHtml = Parser.unescapeEntities(html, false);
        log.info("Jsoup raw " + rawHtml);

        // 2. Parse the raw HTML string
        Document doc = Jsoup.parse(rawHtml);
//        doc.select("br").before("\n");

        // Tell jsoup to keep the pretty-print formatting structure
//        doc.outputSettings().prettyPrint(true);

// Use Jsoup.clean to strip tags but keep formatting layout
//        String text = Jsoup.clean(doc.body().html(), "", Safelist.none(), doc.outputSettings());
//        System.out.println(text.trim());

        // Extract text from the body
//        String text = doc.body().text();

        doc.outputSettings().prettyPrint(false); // Запрещаем jsoup менять структуру пробелов

        // Используем wholeText() вместо text() — он сохраняет все оригинальные \n
        String text = doc.wholeText();

        log.info("Jsoup result " + text.trim());

        return new ResponseEntity<Map<UUID, List<TestDto>>>(
            myCachedService.findAllByEntityIds(uuids),
            HttpStatus.OK);
    }

}
