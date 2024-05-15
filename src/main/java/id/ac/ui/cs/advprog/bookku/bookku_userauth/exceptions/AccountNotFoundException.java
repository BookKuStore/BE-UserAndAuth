package id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions;

public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException() {
        super("Account not found.");
    }
    
}
