package bankingapp.controller;

import bankingapp.model.AccountVo;
import bankingapp.model.Member;
import bankingapp.service.AccountService;

import java.util.List;

public class AccountController {

    private AccountService accountService = new AccountService();

    public void terminationAccount(int id, Member member) {
        accountService.terminationAccount(id, member);
    }

    public List<AccountVo> getAccountList(Member member) {
        return accountService.getAccountList(member);
    }

    public void createAccount(Member member, long product_id) {
        accountService.createAccount(member, product_id);
    }
}
