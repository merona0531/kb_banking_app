package bankingapp.view;

import bankingapp.controller.AccountController;
import bankingapp.dao.ProductDAOImpl;
import bankingapp.model.AccountVo;
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

        while(true) {
            System.out.println();
            System.out.println("+++++++++++++++++++++++++++++++");
            System.out.println("계좌 관리");
            System.out.println("메뉴를 선택하세요");
            System.out.println("+++++++++++++++++++++++++++++++");
            System.out.println("1. 계좌 정보 조회");
            System.out.println("2. 신규 계좌 개설");
            System.out.println("3. 계좌 해지");
            System.out.println("4. 메뉴 화면으로 이동");
            System.out.println();

            System.out.print("선택: ");
            int sel = scanner.nextInt();
            System.out.println();

            switch (sel) {
                case 1:
                    getAccount();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    termination();
                    break;
                case 4:
                    System.out.println("🔙 메인 메뉴로 돌아갑니다.");
                    return;
                default:
                    System.out.println("다시 선택해주세요.");
                    break;
            }
        }
    }

    // 계좌 정보 조회
    private void getAccount() {
        System.out.println("[계좌 정보 조회]");

        String format = "| %-4s | %-15s | %-10s | %-10s | %-12s | %-4s |\n";

        // 테이블 헤더
        System.out.println("+------+-----------------+------------+------------+--------------+--------+");
        System.out.println("| 번호 | 계좌번호         | 종류       | 잔액        | 개설일        | 상태   |");
        System.out.println("+------+-----------------+------------+------------+--------------+--------+");

        // 데이터 출력
        for (AccountVo accountVo : controller.getAccountList(member)) {
            System.out.printf(format,
                    accountVo.getId(),
                    accountVo.getAccountNumber(),
                    accountVo.getProduct(),
                    accountVo.getBalance(),
                    accountVo.getCreatedAt(),
                    accountVo.getStatus());
        }

        // 테이블 바닥선
        System.out.println("+------+-----------------+------------+------------+--------------+--------+");
    }

    // 신규 계좌 개설
    private void createAccount() {
        // 상품 조회 필요\
        new ProductView(new ProductDAOImpl()).printListProducts();
        // member, 상품, 0, date, 정상, 계좌 번호 생성
        System.out.println("원하는 상품을 선택해주세요.");
        long product_id = scanner.nextInt();
        scanner.nextLine();

        controller.createAccount(member, product_id);
        System.out.println("계좌 개설이 완료되었습니다.\n");
    }

    // 해지
    private void termination() {
        getAccount();
        System.out.println("해지하실 계좌를 선택해주세요.");

        // 선택
        System.out.print("선택: ");
        int sel = scanner.nextInt();
        controller.terminationAccount(sel, member);
    }
}
