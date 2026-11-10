package codenomads.flight_tracking.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 400);
        responseBody.put("message", message.isBlank() ? "Validation failed" : message);
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> unreadableBodyHandler(HttpMessageNotReadableException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 400);
        responseBody.put("message", "Malformed request body");
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentHandler(IllegalArgumentException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 400);
        responseBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(responseBody);
    }
}
