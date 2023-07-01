package ru.clevertec.AuthServer.exception;

public class BadRequestBodyException extends RuntimeException {
    public BadRequestBodyException() {
    }

    public BadRequestBodyException(String message) {
        super(message);
    }
}
