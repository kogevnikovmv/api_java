package ru.appapi.controllers;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.appapi.exceptions.AuthException;
import ru.appapi.models.ChangePasswordRequest;
import ru.appapi.models.LoginRequest;
import ru.appapi.models.RegisterRequest;
import ru.appapi.models.User;

import java.util.HashMap;

import static ru.appapi.MyApp.userService;

@Validated
@RestController
@RequestMapping("/user")
public class RegController {

    @PostMapping("/login")
    public String login (@RequestBody LoginRequest request) {
        User user=userService.findUserByLogin(request.getLogin());
        if (user!=null) {
            if (userService.validateUserByLogin(user, request.getPassword())) {
                String token=user.getToken().getTokenValue();
                return "{\"auth_token\": \"Bearer "+token+"\"}";
            } else {throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Wrong login or password");}
        } else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong login or password");}

    }

    //нужно добавить обработку ошибок
    @PostMapping("/register")
    String register (@Valid @RequestBody RegisterRequest request) throws AuthException {
        if (userService.findUserByLogin(request.getLogin())!=null) {
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this username already exists");
            throw new AuthException("Custom exception work! User with this username already exists");
        }
        if (userService.findUserByEmail(request.getEmail())!=null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }
        String token=userService.saveUser(new User(
                request.getLogin(),
                request.getEmail(),
                request.getPassword()
        ));
        return "{\"auth_token\": \"Bearer "+token+"\"}";
    }

    @PostMapping("/chng-psswrd")
    String changePassword (@RequestHeader HashMap<String, String> headers, @RequestBody ChangePasswordRequest request) {
        if (headers.containsKey("authorization")) {
            String token = headers.get("authorization").split(" ")[1];
            String newToken=userService.changePassword(token, request.getPassword());

            if (newToken==null) {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token(?)");
            } else {return "{\"auth_token\": \"Bearer "+newToken+"\"}";}

        } else {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to authorize");}
    }
}
