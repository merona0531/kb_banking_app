package bankingapp.dao;

import bankingapp.model.AccountVo;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    void insert(AccountVo accountVo); // 계좌 추가

    List<AccountVo> getAccounts(long userId); // 유저 계좌 정보 조회(여러개)

    Optional<AccountVo> getAccount(long id); // 계좌 정보 조회(1개)

    void terminateAccount(long id); // 계좌 삭제(상태만 변경)

    void updateBalance(long id, int price); // 잔액 변경

    int getBalance(long id); // 잔액 조회

    void deleteAccount(long id);
}
