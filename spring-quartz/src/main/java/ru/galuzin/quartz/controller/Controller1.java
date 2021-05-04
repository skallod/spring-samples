package ru.galuzin.quartz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller1 {

    @GetMapping("/create/hour/{hour}")
    public void get1(@PathVariable String hour) {
        return newJob()
                .ofType(SampleJob.class)
                .storeDurably()
                .withIdentity(JobKey.jobKey("Qrtz_Job_Detail"))
                .withDescription("Invoke Sample Job service...").build();
    }
}
