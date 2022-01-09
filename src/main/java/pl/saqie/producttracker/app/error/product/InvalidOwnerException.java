package pl.saqie.producttracker.app.error.product;

public class InvalidOwnerException extends Exception{

    public InvalidOwnerException(String message){
        super(message);
    }
}
