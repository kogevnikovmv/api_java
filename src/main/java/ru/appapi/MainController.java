package ru.appapi;

import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

import static ru.appapi.MyApp.userService;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping("/")
    public String home(@RequestHeader HashMap<String, String> headers) {
        if (headers.containsKey("Authorization")) {
            String token = headers.get("Authorization").split(" ")[1];
            User user=userService.validateUserByToken(token);
            if (user!=null) {
                return "{\"message\": "+"\"Hello, "+user.getLogin()+"\"!}";
            }
            else {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token(?)");}
        } else {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to authorize");}
    }
}
