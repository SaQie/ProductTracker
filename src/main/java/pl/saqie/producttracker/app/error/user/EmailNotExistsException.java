package pl.saqie.producttracker.app.error.user;

public class EmailNotExistsException extends Exception{
    public EmailNotExistsException(String message){
        super(message);
    }
}
