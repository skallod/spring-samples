package ru.galuzin.config_server_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigClientMain {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientMain.class);
        System.out.println("System.getenv() = " + System.getenv());
        System.out.println("System.getprop() = " + System.getProperties());
    }
}
