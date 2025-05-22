package bankingapp.dao;

import bankingapp.model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    private Connection conn;

    public MemberDAO(Connection conn) {
        this.conn = conn;
    }

    // 회원 추가
    public void insertMember(Member member) throws SQLException {
        String sql = "INSERT INTO 회원(user_id, name, resident_id, address, phone, email, password, birth_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

    // 회원 조회 (user_id 기준)
    public Member getMemberById(long userId) throws SQLException {
        String sql = "SELECT * FROM 회원 WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
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

    // 회원 정보 수정
    public void updateMember(Member member) throws SQLException {
        String sql = "UPDATE 회원 SET name=?, resident_id=?, address=?, phone=?, email=?, password=?, birth_date=? WHERE user_id=?";
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

    // 회원 삭제
    public void deleteMember(long userId) throws SQLException {
        String sql = "DELETE FROM 회원 WHERE user_id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
        }
    }

    // 전체 회원 조회 (옵션)
    public List<Member> getAllMembers() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM 회원";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                list.add(new Member(
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("resident_id"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDate("birth_date")
                ));
            }
        }
        return list;
    }
}
