package bankingapp.dao;

import bankingapp.model.Member;
import java.sql.*;

public class MemberDAO {

    private final Connection conn;

    public MemberDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ 사용 가능한 가장 작은 user_id(1~999)를 찾는 메서드 추가
    public long findNextAvailableUserId() throws SQLException {
        String sql = "SELECT user_id FROM member WHERE user_id <= 999 ORDER BY user_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            long expected = 1;
            while (rs.next()) {
                long actual = rs.getLong("user_id");
                if (actual != expected) {
                    return expected;
                }
                expected++;
            }
            return expected <= 999 ? expected : -1;
        }
    }

    // ✅ 회원가입 - user_id를 지정해서 삽입
    public void insertMember(Member member) throws SQLException {
        String sql = "INSERT INTO member(user_id, name, resident_id, address, phone, email, password, birth_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, member.getUserId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getResidentId());
            pstmt.setString(4, member.getAddress());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getEmail());
            pstmt.setString(7, member.getPassword());
            pstmt.setDate(8, member.getBirthDate());
            pstmt.executeUpdate();
        }
    }

    // 로그인 - 이메일 + 비밀번호로 회원 조회
    public Member findByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                            rs.getLong("user_id"),
                            rs.getString("name"),
                            rs.getString("resident_id"),
                            rs.getString("address"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getDate("birth_date")
                    );
                }
            }
        }
        return null;
    }

    // 마이페이지 - 회원 정보 수정
    public void updateMember(Member member) throws SQLException {
        String sql = "UPDATE member SET name=?, resident_id=?, address=?, phone=?, email=?, password=?, birth_date=? WHERE user_id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getResidentId());
            pstmt.setString(3, member.getAddress());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, member.getPassword());
            pstmt.setDate(7, member.getBirthDate());
            pstmt.setLong(8, member.getUserId());
            pstmt.executeUpdate();
        }
    }

    // 마이페이지 - 회원 삭제
    public void deleteMember(long userId) throws SQLException {
        String sql = "DELETE FROM member WHERE user_id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
        }
    }
}
