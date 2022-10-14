package com.example.demo.exeption.user;

public class DuplicateStudentIdException extends RuntimeException {

    public DuplicateStudentIdException(String studentId) {
        super(studentId);
    }
}
