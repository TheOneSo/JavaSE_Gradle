package com.oneso.exceptions;

public class ComponentException extends Exception {

    public ComponentException(String info, Exception e) {
        super(info, e);
    }
}
