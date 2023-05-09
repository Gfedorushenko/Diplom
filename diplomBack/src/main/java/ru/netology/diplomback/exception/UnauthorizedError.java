package ru.netology.diplomback.exception;

public class UnauthorizedError extends RuntimeException {
    public UnauthorizedError(String msg) {
        super(msg);
    }
}
