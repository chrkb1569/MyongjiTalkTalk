package com.example.demo.exeption.user;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super(email);
    }
}
