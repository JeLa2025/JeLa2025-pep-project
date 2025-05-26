package Service;

// imports Account class
import Model.Account;
import Model.Message;

import java.util.List;

import DAO.AccountDAO;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * TODO: insert a new Account into the Account table
     * will not contain an account_id
     * the username is not blank
     * the password is at least 4 characters long
     * an Account with that username does not already exist. 
     */
    public Account addAccount(Account account) {
        Account acc;
        // get username
        String username = account.getUsername();
        // get password 
        String password = account.getPassword();
        
        // check username and password constraints
        if (username.length() > 0 && password.length() >= 4) {
            // check if username already exist in the table
            acc = accountDAO.getAccountFromUsername(username);
            // if username doesn't exist in the table
            if (acc == null) {
                return accountDAO.insertAccount(account);
            }
        }
        return null;
    }
     /**
      * TODO: verify my login
      * the username and password provided match a real account existing on the database. 
      */
    public Account verifyLogin(Account account) {
        // get username and password form account
        String username = account.getUsername();
        String password = account.getPassword();

        Account val = accountDAO.getAccountFromUsernameAndPassword(username, password);
        return val;
    }

    /**
     * TODO: retrive all messages written by a particular user
     */
    public List<Message> getAllMessages(int account_id) {
        return accountDAO.getAllMessagesByAccount(account_id);
    }

}
