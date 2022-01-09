package pl.saqie.producttracker.app.error.product;

public class CooldownException extends Exception {

    public CooldownException(String message) {
        super(message);
    }
}
