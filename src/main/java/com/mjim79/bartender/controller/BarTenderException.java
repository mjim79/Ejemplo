package com.mjim79.bartender.controller;

public class BarTenderException extends RuntimeException {

    private static final long serialVersionUID = 1731719002622826927L;

    protected BarTenderException() {
        this("");
    }

    protected BarTenderException(Throwable cause) {
        this("", cause);
    }

    protected BarTenderException(String message) {
        super(message);
    }

    public BarTenderException(String message, Throwable cause) {
        super(message, cause);
    }

}
