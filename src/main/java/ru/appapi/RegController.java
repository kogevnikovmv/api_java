package ru.appapi;

import models.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Objects;

import static ru.appapi.MyApp.userService;

@RestController
@RequestMapping("/user")
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
        String token=userService.saveUser(new User(
                request.getLogin(),
                request.getEmail(),
                request.getPassword()
        )); // возвращается User, а не токен

        /*if (userService.saveUser(new User(
                request.getLogin(),
                request.getEmail(),
                request.getPassword()
                )
        )!=null) {return "{\"token\": \""+user.getEmail()+"\"}";}
        else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something wrong");}*/

        //else {return "{\"message\": \"new account created\"}";}
        return "";
    }

    @PostMapping("/chng-psswrd")
    String changePassword (@RequestHeader HashMap<String, String> headers, @RequestBody ChangePasswordRequest request) {
        if (headers.containsKey("Authorization")) {
            String token = headers.get("Authorization:").split(" ")[1];
            if (userService.changePassword(token, request.getPassword())==null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token(?)");} //?
            else {throw new ResponseStatusException(HttpStatus.OK, "Password changed");}
        } else {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to authorize");}
    }

    @PostMapping("/test")
    String test (@RequestBody LoginRequest request) {
        User user=userService.findUserByLogin(request.getLogin());
        return "{\"message\": "+"\""+user.getHashPassword()+"\"}";
    }
}
