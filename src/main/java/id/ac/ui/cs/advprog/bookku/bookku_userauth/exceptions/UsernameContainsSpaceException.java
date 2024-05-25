package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class UsernameContainsSpaceException extends RuntimeException {

    public UsernameContainsSpaceException() {
        super("Username cannot contains space.");
    }
    
}
