package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class BlankInputException extends RuntimeException {

    public BlankInputException() {
        super("Fields cannot be blank");
    }
    
}
