package org.typecrafters.teambuild.domain.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.typecrafters.teambuild.domain.enums.AppStatusCode;

public class AppException extends RuntimeException {
    private final AppStatusCode code;

    private AppException(AppStatusCode code, String message) {
        super(message);
        this.code = code;
    }

    private AppException(AppStatusCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    private AppException(AppStatusCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public static AppException of(AppStatusCode code, String message) {
        return new AppException(code, message);
    }

    public static AppException of(AppStatusCode code, String message, Throwable cause) {
        return new AppException(code, message, cause);
    }

    public static AppException of(AppStatusCode code, Throwable cause) {
        return new AppException(code, cause);
    }

    public static AppException badRequest(String message) {
        return new AppException(AppStatusCode.BAD_REQUEST, message);
    }

    public static AppException unauthorized(String message) {
        return new AppException(AppStatusCode.UNAUTHORIZED, message);
    }

    public static AppException forbidden(String message) {
        return new AppException(AppStatusCode.FORBIDDEN, message);
    }

    public static AppException notFound(String message) {
        return new AppException(AppStatusCode.NOT_FOUND, message);
    }

    public static AppException conflict(String message) {
        return new AppException(AppStatusCode.CONFLICT, message);
    }

    public static AppException gone(String message) {
        return new AppException(AppStatusCode.GONE, message);
    }

    public static AppException unprocessableContent(String message) {
        return new AppException(AppStatusCode.UNPROCESSABLE_CONTENT, message);
    }

    public static AppException tooManyRequests(String message) {
        return new AppException(AppStatusCode.TOO_MANY_REQUESTS, message);
    }

    public static AppException internalServerError(String message) {
        return new AppException(AppStatusCode.INTERNAL_SERVER_ERROR, message);
    }

    public AppStatusCode getCode() {
        return code;
    }

    public static HttpStatus toHttpStatus(AppStatusCode code) {
        return Objects.requireNonNull(
                HttpStatus.resolve(code.getValue()),
                "No Spring HttpStatus for code " + code.getValue());
    }

    public ResponseStatusException toHttpException() {
        return new ResponseStatusException(toHttpStatus(code), getMessage(), this);
    }
}
