package codenomads.experience_search.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 */
@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> businessExceptionHandler(CustomException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", e.getCode());
        responseBody.put("message", e.getMessage());
        return ResponseEntity.status(e.getCode()).body(responseBody);
    }
}
