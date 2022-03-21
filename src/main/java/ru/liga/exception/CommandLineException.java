package ru.liga.exception;

public class CommandLineException extends RuntimeException {
    public CommandLineException() {
    }

    public CommandLineException(String message) {
        super(message);
    }
}
