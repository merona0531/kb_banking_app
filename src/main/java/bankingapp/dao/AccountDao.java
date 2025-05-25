package bankingapp.dao;

import bankingapp.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    void insert(Account account); // 계좌 추가

    List<Account> getAccounts(long userId); // 유저 계좌 정보 조회(여러개)

    Optional<Account> getAccount(long id); // 계좌 정보 조회(1개)

    void terminateAccount(long id); // 계좌 삭제(상태만 변경)

    void updateBalance(long id, int price); // 잔액 변경

    int getBalance(long id); // 잔액 조회
}
