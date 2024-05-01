package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Token is invalid.");
    }
    
}
