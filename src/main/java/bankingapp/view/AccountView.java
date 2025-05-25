package bankingapp.view;

import bankingapp.controller.AccountController;
import bankingapp.model.Account;
import bankingapp.model.Member;

import java.util.Scanner;

public class AccountView {

    private Member member;
    private Scanner scanner;
    private AccountController controller = new AccountController();

    public AccountView(Member member) {
        this.member = member;
        scanner = new Scanner(System.in);
    }

    public void display() {
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println("계좌 관리");
        System.out.println("메뉴를 선택하세요");
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println("1. 계좌 정보 조회");
        System.out.println("2. 신규 계좌 개설");
        System.out.println("3. 계좌 해지");
        System.out.println("4. 이전 화면으로 이동");
        System.out.println();
        while(true) {
            System.out.print("선택: ");
            int sel = scanner.nextInt();

            switch (sel) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:

                    break;
                default:
                    System.out.println("다시 선택해주세요.");
                    break;
            }
        }
    }

    private void createAccount() {

    }
    // 해지
    private void termination() {
        System.out.println("해지하실 계좌를 선택해주세요.");
        String format = "| %-4s | %-15s | %-10s | %-10s | %-10s | %-12s |\n";

        // 테이블 헤더
        System.out.println("+------+-----------------+------------+------------+------------+--------------+--------+");
        System.out.printf(format, "번호", "계좌번호", "종류", "카테고리", "잔액", "개설일");
        System.out.println("+------+-----------------+------------+------------+------------+--------------+--------+");

        // 데이터 출력
        int index = 1;
        // 상품 정보 데이터 가져오기
        // 카테고리 이름 가져오기
        for (Account account : controller.getAccountList(member)) {
            if(account.getStatus().equals("해지")) continue;
            System.out.printf(format,
                    index++,
                    account.getAccountNumber(),
                    account.getProduct(),
                    account.getCategory(),
                    account.getBalance(),
                    account.getCreatedAt());
        }

        // 테이블 바닥선
        System.out.println("+------+-----------------+------------+------------+------------+--------------+--------+");

        // 선택
        System.out.print("선택: ");
        int sel = scanner.nextInt();
        controller.terminationAccount(sel, member);
    }
}
