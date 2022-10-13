package com.example.demo.exeption.user;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super(username);
    }
}
