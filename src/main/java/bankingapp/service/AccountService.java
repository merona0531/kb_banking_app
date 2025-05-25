package bankingapp.service;

import bankingapp.dao.AccountDao;
import bankingapp.dao.AccountDaoImpl;
import bankingapp.model.Account;
import bankingapp.model.Member;

import java.util.List;

public class AccountService {

    private AccountDao accountDao = new AccountDaoImpl();

    // 계좌 해지
    public void terminationAccount(int id, Member member) {
        accountDao.terminateAccount(id);
    }

    // 계좌 리스트 조회
    public List<Account> getAccountList(Member member) {
        return accountDao.getAccounts(member.getUserId());
    }
}
