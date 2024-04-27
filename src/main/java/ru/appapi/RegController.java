package ru.appapi;

import models.BaseResponse;
import models.LoginRequest;
import models.RegisterRequest;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static ru.appapi.MyApp.userService;

@RestController
@RequestMapping()
public class RegController {

    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    //private UserService userService = new UserService();

    @PostMapping("/login")
    public BaseResponse login (@RequestBody LoginRequest request) {
        final BaseResponse response;
        if (Objects.equals(request.getLogin(), "admin") && Objects.equals(request.getPassword(), "goodpassword")) {
            response= new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong login or password");
        }
        return response;
    }

    @PostMapping("/register")
    String register (@RequestBody RegisterRequest request) {
        //return "{\"email\": \""+user.getEmail()+"\"}";*/
        if (userService.saveUser(new User(
                request.getLogin(),
                request.getEmail(),
                request.getPassword()
                )
        )==null) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something wrong");}
        else {return "{\"message\": \"new account created\"}";}
    }
}
