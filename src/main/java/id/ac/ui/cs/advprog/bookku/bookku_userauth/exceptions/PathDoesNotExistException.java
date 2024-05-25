package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class PathDoesNotExistException extends RuntimeException {

    public PathDoesNotExistException() {
        super("Path does not exist. Please check your path.");
    }
    
}
