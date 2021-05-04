package ru.galuzin.quartz;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication

public class QuartzApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(QuartzApp.class).bannerMode(Banner.Mode.OFF).run(args);
    }
}
