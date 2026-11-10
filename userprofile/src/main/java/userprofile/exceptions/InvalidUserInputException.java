package userprofile.exceptions;

public class InvalidUserInputException extends CustomException {
    public static final int code = 400;
    public InvalidUserInputException(String message) {
        super(message, code);
    }
}
