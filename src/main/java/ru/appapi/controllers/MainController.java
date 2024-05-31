package ru.appapi.controllers;

import ru.appapi.models.User;
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
        if (headers.containsKey("authorization")) {
            String token = headers.get("authorization").split(" ")[1];
            User user=userService.validateUserByToken(token);
            if (user!=null) {
                return "{\"message\": "+"\"Hello, "+user.getLogin()+"!\"}";
            }
            else {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"err-message\": \"Invalid token(?)\"}");}
        } else {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"err-message\": \"You need to authorize\"}");}
    }
}
