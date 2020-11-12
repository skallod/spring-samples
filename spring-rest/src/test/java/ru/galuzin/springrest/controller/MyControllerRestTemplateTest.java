package ru.galuzin.springrest.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyControllerRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Test
    public void test1() {
        System.out.println("restTemplate = " + restTemplate);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/testpost",
                HttpMethod.POST,
                new HttpEntity<>(null),
                String.class);
        //then
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
