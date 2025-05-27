package bankingapp.service;

import bankingapp.dao.AccountDao;
import bankingapp.dao.AccountDaoImpl;
import bankingapp.model.AccountVo;
import bankingapp.model.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class AccountService {

    private AccountDao accountDao = new AccountDaoImpl();

    // 계좌 해지
    public void terminationAccount(int id, Member member) {
        accountDao.terminateAccount(id);
    }

    // 계좌 리스트 조회
    public List<AccountVo> getAccountList(Member member) {
        return accountDao.getAccounts(member.getUserId());
    }

    public void createAccount(Member member, long product_id) {
        accountDao.insert(AccountVo.builder()
                .id(1L) // 아이디 랜덤 설정
                .userId(member.getUserId())
                .productId(product_id)
                .balance(0)
                .createdAt(LocalDate.now())
                .accountNumber(createAccountNumber())
                .build());
    }

    private String createAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10); // 0~9
            sb.append(digit);
        }

        return sb.toString();
    }
}
