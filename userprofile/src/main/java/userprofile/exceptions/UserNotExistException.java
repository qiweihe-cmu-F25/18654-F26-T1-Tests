package userprofile.exceptions;

public class UserNotExistException extends CustomException {
    public static final int code = 1401;
    public UserNotExistException(String message) {
            super(message, code);
        }
}
