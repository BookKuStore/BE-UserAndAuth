package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class UsernameAlreadyExistException extends RuntimeException {
    
    public UsernameAlreadyExistException() {
        super("Username already exist.");
    }
    
}
