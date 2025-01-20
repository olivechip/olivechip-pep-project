package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public boolean checkForExistingAccountId(int accountId) {
        return accountDAO.checkForExistingAccountId(accountId);
    }

    public boolean checkForExistingUsername(String username) {
        return accountDAO.checkForExistingUsername(username);
    }

    public Account register(String username, String password) {
        return accountDAO.register(username, password);
    }

    public Account login(Account account) {
        return accountDAO.login(account);
    }
}