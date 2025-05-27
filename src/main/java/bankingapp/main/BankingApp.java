package bankingapp.main;
import bankingapp.controller.AuthController;
import bankingapp.dao.ProductDAO;
import bankingapp.dao.ProductDAOImpl;
import bankingapp.view.ProductView;
import java.util.Scanner;

public class BankingApp {
    private Scanner scanner;
    private AuthController authController;

    private ProductDAO productDAO;
    private ProductView productView;

    public BankingApp() {
        // 데이터베이스 연결 및 DAO 초기화
        // ProductDAOImpl 생성자에서 연결이 이루어집니다.
        this.scanner = new Scanner(System.in);
        this.authController = new AuthController(this.scanner);
        this.productDAO = new ProductDAOImpl();
        // UI 클래스 초기화 (DAO 객체를 전달하여 데이터 접근 가능하게 함)
        this.productView = new ProductView(productDAO);
    }

    public void start() {
        System.out.println("### 🏦 뱅킹 앱 시스템 시작 ###");

        while (true) {
            printMainMenu();
            int choice = getUserInput(scanner);

            switch (choice) {
                case 1:
                    authController.register();
                    break;
                case 2:
                    authController.login();
                    break;
                case 3:
                    productView.showProductMenu(scanner); // 상품 관리 메뉴로 이동
                    break;
                case 0:
                    System.out.println("### 뱅킹 앱 시스템 종료 ###");
                    return; // 애플리케이션 종료
                default:
                    System.out.println("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n--- 메인 메뉴 ---");
        System.out.println("1. 회원가입 화면");
        System.out.println("2. 로그인 화면");
        System.out.println("0. 종료");
        System.out.print("메뉴 선택: ");
    }

    private int getUserInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("❗️ 숫자만 입력해주세요.");
            scanner.next(); // 잘못된 입력 버리기
            System.out.print("메뉴 선택: ");
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기 (숫자 입력 후 엔터 처리)
        return input;
    }

    public static void main(String[] args) {
        BankingApp app = new BankingApp();
        app.start();

        /*// 애플리케이션 종료 시 데이터베이스 연결 닫기
        if (app.productDAO instanceof ProductDAOImpl) {
            ((ProductDAOImpl) app.productDAO).closeConnection();
        }*/
    }
}

