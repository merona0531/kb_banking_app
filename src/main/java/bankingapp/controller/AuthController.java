package bankingapp.controller;

import bankingapp.model.Member;
import bankingapp.dao.MemberDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class AuthController {
    private final Scanner scanner;

    public AuthController(Scanner scanner) {
        this.scanner = scanner;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/bank", "root", "0531");
    }

    public void login() {
        try (Connection conn = getConnection()) {
            MemberDAO dao = new MemberDAO(conn);

            System.out.print("이메일: ");
            String email = scanner.nextLine();
            System.out.print("비밀번호: ");
            String password = scanner.nextLine();

            Member member = dao.findByEmailAndPassword(email, password);
            if (member != null) {
                System.out.println("로그인 성공! Welcome " + member.getName() + "님");
                new MenuController(scanner, conn, member).start();
            } else {
                System.out.println("이메일 또는 비밀번호가 일치하지 않습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        try (Connection conn = getConnection()) {
            MemberDAO dao = new MemberDAO(conn);
            Member newMember = new Member();

            // 👉 다음 사용 가능한 user_id 찾기 (1~999 중)
            long newUserId = dao.findNextAvailableUserId();
            if (newUserId == -1) {
                System.out.println("회원 수가 제한(999명) 초과되어 가입할 수 없습니다.");
                return;
            }
            newMember.setUserId(newUserId);

            System.out.print("이름: ");
            newMember.setName(scanner.nextLine());

            System.out.print("주민등록번호: ");
            newMember.setResidentId(scanner.nextLine());

            System.out.print("주소: ");
            newMember.setAddress(scanner.nextLine());

            System.out.print("휴대폰번호: ");
            newMember.setPhone(scanner.nextLine());

            System.out.print("이메일: ");
            newMember.setEmail(scanner.nextLine());

            System.out.print("비밀번호: ");
            newMember.setPassword(scanner.nextLine());

            System.out.print("생년월일 (yyyy-mm-dd): ");
            newMember.setBirthDate(java.sql.Date.valueOf(scanner.nextLine()));

            dao.insertMember(newMember);

            System.out.println("회원가입이 완료되었습니다. 회원번호(user_id): " + newUserId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
