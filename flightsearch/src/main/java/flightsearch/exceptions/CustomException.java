package flightsearch.exceptions;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    public String message;
    public int code;
    public CustomException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
