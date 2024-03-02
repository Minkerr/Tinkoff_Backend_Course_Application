package edu.java.bot.dto;

public class ApiErrorException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ApiErrorException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }
}

