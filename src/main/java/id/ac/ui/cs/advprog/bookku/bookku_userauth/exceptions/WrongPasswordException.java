package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class WrongPasswordException extends RuntimeException {
    
    public WrongPasswordException() {
        super("Password is incorrect.");
    }

}
