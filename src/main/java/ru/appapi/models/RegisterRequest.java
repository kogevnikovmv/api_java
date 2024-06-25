package ru.appapi.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class RegisterRequest {

    @Pattern(regexp="^[a-zA-Z0-9_-]{2,16}$")
    //длина логина от 2 до 16 символов, заглавные и прописные буквы, латаница, разрешены символы "-" "_".
    private String login;
    @Email
    private String email;
    @Pattern(regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{8,}$",
            message = "длина пароля от 8 до 30, обязательны числа, спец символы \"@$!%*?&\", заглавные и прописные буквы, латиница."
    )
    //длина пароля от 8 до 30, обязательны числа, спец символы "@$!%*?&", заглавные и прописные буквы, латиница.
    private String password;

}

