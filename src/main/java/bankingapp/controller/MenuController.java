package bankingapp.controller;

import bankingapp.model.Member;

import java.sql.Connection;
import java.util.Scanner;

public class MenuController {
    private final Scanner scanner;
    private final Connection conn;
    private final Member member;

    public MenuController(Scanner scanner, Connection conn, Member member) {
        this.scanner = scanner;
        this.conn = conn;
        this.member = member;
    }

    public void start() {
        while (true) {
            System.out.println("\n+++++++++++++++++++++++++++++++");
            System.out.println("로그인 성공! Welcome " + member.getName() + "님");
            System.out.println("메뉴를 선택하세요");
            System.out.println("+++++++++++++++++++++++++++++++");
            System.out.println("1. 계좌 관리");
            System.out.println("2. 거래 내역");
            System.out.println("3. 마이페이지");
            System.out.println("4. 로그아웃");

            System.out.print("선택: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // new AccountController(scanner, conn, member).start();
                    break;
                case "2":
                    // new TransactionController(scanner, conn, member).start();
                    break;
                case "3":
                    // new MyPageController(scanner, conn, member).start();
                    break;
                case "4":
                    System.out.println("로그아웃합니다.");
                    return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }
}
