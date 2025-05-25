package bankingapp.controller;

import bankingapp.model.Account;
import bankingapp.model.Member;
import bankingapp.service.AccountService;

import java.util.List;

public class AccountController {

    private AccountService accountService = new AccountService();

    public void terminationAccount(int id, Member member) {
        accountService.terminationAccount(id, member);
    }

    public List<Account> getAccountList(Member member) {
        return accountService.getAccountList(member);
    }
}
