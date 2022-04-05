package bvvs.chatserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ValidationException {
    public ResourceNotFoundException(Map<String, String> errors) {
        super(errors);
    }
}
