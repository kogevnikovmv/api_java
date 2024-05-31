package ru.appapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.appapi.services.UserService;


@SpringBootApplication

public class MyApp {
    public static UserService userService = new UserService();
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}