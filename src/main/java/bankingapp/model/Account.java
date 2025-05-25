package bankingapp.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private long id;
    private long userId; // 회원 id
    private long productId; // 상품 id
    private long categoryId; // 카테고리 id
    private int balance; // 잔액
    private LocalDate createdAt; // 개설일
    private String status; // 해지 상태
    private String accountNumber; // 계좌번호

    //카테고리명(join)
    private String category;
    // 상품명(join)
    private String product;
}
