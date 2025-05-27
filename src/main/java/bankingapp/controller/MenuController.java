package bankingapp.controller;

import bankingapp.dao.ProductDAO;
import bankingapp.dao.ProductDAOImpl;
import bankingapp.model.Member;
import bankingapp.view.AccountView;
import bankingapp.view.ProductView;
import bankingapp.dao.AccountDao;
import bankingapp.dao.AccountDaoImpl;

import java.sql.Connection;
import java.util.Scanner;

public class MenuController {
    private final Scanner scanner;
    private final Member member;
    private ProductDAO productDAO;
    private ProductView productView;
    private AccountDao accountDao;
    private AccountView accountView;

    public MenuController(Scanner scanner, Member member) {
        this.scanner = scanner;
        this.member = member;
        this.productDAO = new ProductDAOImpl();
        // UI 클래스 초기화 (DAO 객체를 전달하여 데이터 접근 가능하게 함)
        this.productView = new ProductView(productDAO);
        this.accountDao = new AccountDaoImpl();
    }

    public void start() {
        while (true) {
            System.out.println("\n+++++++++++++++++++++++++++++++");
            System.out.println("로그인 성공! Welcome " + member.getName() + "님");
            System.out.println("메뉴를 선택하세요");
            System.out.println("+++++++++++++++++++++++++++++++");
            System.out.println("1. 계좌 관리");
            System.out.println("2. 상품 관리");
            System.out.println("3. 마이페이지");
            System.out.println("4. 로그아웃");
            System.out.print("선택: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    new AccountView(member).display();
                    break;
                case "2":
                    productView.showProductMenu(scanner);
                    break;
                case "3":
                    boolean continueMenu = new MyPageController(scanner, conn, member).start();
                    if (!continueMenu) return; // 탈퇴 시 바로 로그아웃
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
