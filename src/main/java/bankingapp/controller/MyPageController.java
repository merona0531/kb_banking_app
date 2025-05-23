package bankingapp.controller;

import bankingapp.dao.MemberDAO;
import bankingapp.model.Member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MyPageController {
    private final Scanner scanner;
    private final Connection conn;
    private final Member member;

    public MyPageController(Scanner scanner, Connection conn, Member member) {
        this.scanner = scanner;
        this.conn = conn;
        this.member = member;
    }

    // 탈퇴 시 false 반환, 나머지는 true
    public boolean start() {
        while (true) {
            System.out.println("\n--- 마이페이지 ---");
            System.out.println("1. 내 정보 조회");
            System.out.println("2. 내 정보 수정");
            System.out.println("3. 회원 탈퇴");
            System.out.println("4. 뒤로 가기");
            System.out.print("선택: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    viewProfile();
                    break;
                case "2":
                    updateProfile();
                    break;
                case "3":
                    boolean deleted = deleteAccount();
                    if (deleted) return false;  // 탈퇴 시 종료
                    break;
                case "4":
                    return true; // 뒤로가기
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private void viewProfile() {
        System.out.println("\n[회원 정보]");
        System.out.println("이름: " + member.getName());
        System.out.println("주민등록번호: " + member.getResidentId());
        System.out.println("주소: " + member.getAddress());
        System.out.println("휴대폰번호: " + member.getPhone());
        System.out.println("이메일: " + member.getEmail());
        System.out.println("생년월일: " + member.getBirthDate());
    }

    private void updateProfile() {
        try {
            MemberDAO dao = new MemberDAO(conn);

            System.out.print("새 이름 (" + member.getName() + "): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) member.setName(name);

            System.out.print("새 주민등록번호 (" + member.getResidentId() + "): ");
            String rid = scanner.nextLine();
            if (!rid.isEmpty()) member.setResidentId(rid);

            System.out.print("새 주소 (" + member.getAddress() + "): ");
            String addr = scanner.nextLine();
            if (!addr.isEmpty()) member.setAddress(addr);

            System.out.print("새 휴대폰번호 (" + member.getPhone() + "): ");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) member.setPhone(phone);

            System.out.print("새 이메일 (" + member.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) member.setEmail(email);

            System.out.print("새 비밀번호: ");
            String pw = scanner.nextLine();
            if (!pw.isEmpty()) member.setPassword(pw);

            System.out.print("새 생년월일 (" + member.getBirthDate() + ") (yyyy-mm-dd): ");
            String bd = scanner.nextLine();
            if (!bd.isEmpty()) member.setBirthDate(java.sql.Date.valueOf(bd));

            dao.updateMember(member);
            System.out.println("회원 정보가 수정되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteAccount() {
        System.out.print("정말로 탈퇴하시겠습니까? (yes/no): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            try {
                MemberDAO dao = new MemberDAO(conn);
                dao.deleteMember(member.getUserId());
                System.out.println("회원 탈퇴가 완료되었습니다.");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("탈퇴가 취소되었습니다.");
        }
        return false;
    }
}
