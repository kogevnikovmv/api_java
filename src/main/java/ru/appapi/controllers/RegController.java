package ru.appapi.controllers;

import ru.appapi.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.appapi.models.ChangePasswordRequest;
import ru.appapi.models.LoginRequest;
import ru.appapi.models.RegisterRequest;
import ru.appapi.models.User;

import java.util.HashMap;

import static ru.appapi.MyApp.userService;

@RestController
@RequestMapping("/user")
public class RegController {

    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;


    @PostMapping("/login")
    public String login (@RequestBody LoginRequest request) {
        User user=userService.findUserByLogin(request.getLogin());
        if (user!=null) {
            if (userService.validateUserByLogin(user, request.getPassword())) {
                String token=user.getToken().getTokenValue();
                return "{\"auth_token\": \"Bearer "+token+"\"}";
            } else {
                //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong login or password");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"err-message\": \"Wrong login or password\"}");
            }
        } else {
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong login or password");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"err-message\": \"Wrong login or password\"}");
        }

    }

    @PostMapping("/register")
    String register (@RequestBody RegisterRequest request) {
        if (userService.findUserByLogin(request.getLogin())!=null) {
            return "{\"err-message\": \"User with this username already exists\"}";
        }
        if (userService.findUserByEmail(request.getEmail())!=null) {
            return "{\"err-message\": \"User with this e-mail already exists\"}";
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
        if (headers.containsKey("Authorization")) {
            String token = headers.get("Authorization").split(" ")[1];
            String newToken=userService.changePassword(token, request.getPassword());
            if (newToken==null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"err-message\": \"Invalid token(?)\"}");} //?
            else {throw new ResponseStatusException(HttpStatus.OK, "{\"auth_token\": \"Bearer "+newToken+"\"}");}
        } else {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"err-message\": \"You need to authorize\"}");}
    }
}
