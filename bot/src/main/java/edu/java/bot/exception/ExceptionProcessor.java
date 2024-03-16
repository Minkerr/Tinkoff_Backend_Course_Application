package edu.java.bot.exception;

import edu.java.bot.dto.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionProcessor {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiErrorException.class)
    public ApiErrorResponse handleException(ApiErrorException exception) {
        return new ApiErrorResponse(
            "Api Error Exception",
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            getListStringStackTrace(exception)
        );
    }

    private List<String> getListStringStackTrace(Exception exception) {
        return Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();
    }
}
