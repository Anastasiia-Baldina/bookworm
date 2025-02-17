package org.vse.bookworm.parser.exception;

public class IllegalFb2FormatException extends RuntimeException {
    public IllegalFb2FormatException() {
        super("Документ не соответствует формату fb2");
    }
}
