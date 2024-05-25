package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class ExpiredTokenException extends RuntimeException {
    
    public ExpiredTokenException() {
        super("Token has expired.");
    }

}
