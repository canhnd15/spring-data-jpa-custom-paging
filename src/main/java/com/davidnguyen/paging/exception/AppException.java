package com.laptrinhb2a.PagingCustom.exception;

import org.hibernate.service.spi.ServiceException;

import java.util.List;

public class AppException extends ServiceException {
    private String errorCode;
    private String message;
    private List<String> errorField;
    private String type;

    public AppException(String error, String message) {
        super(message);
        this.message = message;
        this.errorCode = error;
    }

    public AppException(String error, String message, List<String> errorField) {
        super(message);
        this.message = message;
        this.errorCode = error;
        this.errorField = errorField;
    }

    public AppException(String error, List<String> errorField) {
        super("");
        this.errorCode = error;
        this.errorField = errorField;
    }

    public AppException(String error) {
        super("");
        this.errorCode = error;
    }
}
