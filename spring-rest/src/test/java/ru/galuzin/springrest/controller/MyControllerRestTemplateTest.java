package ru.galuzin.springrest.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MyControllerRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void test1() {
        System.out.println("restTemplate = " + restTemplate);
        int i = 0;
        while (true) {
            String owner = i % 2 == 0 ? "selfcheck" : "dbo";
            i++;
            ResponseEntity<String> response = restTemplate.exchange(
                "/getTasks?name="+owner,
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class);
            //then
            Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
