package com.jotacode.polimarket.models.dao.exceptions;

public class NonexistentEntityException extends Exception {
    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonexistentEntityException(String message) {
        super(message);
    }
}