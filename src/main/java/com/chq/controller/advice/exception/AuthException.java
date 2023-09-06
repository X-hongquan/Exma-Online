package com.chq.controller.advice.exception;

public class AuthException extends Exception{

    private String message;

    public AuthException(String message) {
        super(message);
        this.message = message;
    }



}
