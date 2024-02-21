package br.com.rinhaq1.exception;

public class UnprocessableEntity extends RuntimeException {

    public UnprocessableEntity(String message) {
        super(message);
    }
}