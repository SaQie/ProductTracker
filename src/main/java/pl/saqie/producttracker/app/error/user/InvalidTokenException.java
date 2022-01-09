package pl.saqie.producttracker.app.error.user;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message){
        super(message);
    }
}
