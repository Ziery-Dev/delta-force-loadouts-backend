package com.ziery.DeltaForceLoadouts.exception;


public class LoginBlockedException extends RuntimeException {
    public LoginBlockedException(String message) {
        super(message);
    }
}