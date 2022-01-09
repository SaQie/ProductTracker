package pl.saqie.producttracker.app.error.user;

public class PasswordNotMatchException extends Exception {

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
