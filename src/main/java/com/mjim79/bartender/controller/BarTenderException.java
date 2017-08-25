package com.mjim79.bartender.controller;

public class BarTenderException extends RuntimeException {

    private static final long serialVersionUID = 1731719002622826927L;

    protected BarTenderException() {
        this("");
    }

    protected BarTenderException(Throwable causa) {
        this("", causa);
    }

    protected BarTenderException(String mensaje) {
        super(mensaje);
    }

    public BarTenderException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}
