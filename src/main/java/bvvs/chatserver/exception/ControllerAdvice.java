package bvvs.chatserver.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler({ValidationException.class})
    public Map<String, String> handle(ValidationException e) {
        return e.getErrors();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handle(ResourceNotFoundException e) {
        return e.getErrors();
    }
}
