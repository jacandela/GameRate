package com.gamerate.gamerate.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {
    private int code;
    private String message;
    private String cause;

    private Response(int errorCode, String errorMensage) {
        this.code = errorCode;
        this.message = errorMensage;
    }

    private Response(int errorCode, String errorMensage, String cause) {
        this.code = errorCode;
        this.message = errorMensage;
        this.cause = cause;
    }

    public static Response validationError(String errorMensage) {
        return new Response(HttpStatus.BAD_REQUEST.value(), errorMensage);
    }

    public static Response generalError(int code, String mensaje) {
        return new Response(code, mensaje);
    }

    public static Response generalError(int code, String mensaje, String cause) {
        return new Response(code, mensaje, cause);
    }

    public static Response ok(String message) {
        return new Response(HttpStatus.OK.value(), message);
    }
}
