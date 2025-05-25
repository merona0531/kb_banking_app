package bankingapp.dao;

import bankingapp.model.Account;
import bankingapp.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    private final Connection conn = JDBCUtil.getConnection();

    @Override
    public void insert(Account account) {
        String sql = "insert into account values(?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, account.getId());
            pstmt.setLong(2, account.getUserId());
            pstmt.setLong(3, account.getProductId());
            pstmt.setLong(4, account.getCategoryId());
            pstmt.setInt(5, account.getBalance());
            pstmt.setDate(6, Date.valueOf(account.getCreatedAt()));
            pstmt.setString(7, account.getStatus());
            pstmt.setString(8, account.getAccountNumber());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Account> getAccounts(long userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account " +
                "LEFT JOIN category ON category.category_id = account.category_id " +
                "LEFT JOIN product ON product.product_id = account.product_id " +
                "WHERE account.user_id = ?";
        //String sql = "select * from account where user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Account account = map(rs);
                account.setCategory(rs.getString("category_name"));
                account.setProduct(rs.getString("product_name"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    @Override
    public Optional<Account> getAccount(long id) {
        Account account;
        //String sql = "select * from account where id = ?";
        String sql = "SELECT * FROM account " +
                "LEFT JOIN category ON category.category_id = account.category_id " +
                "LEFT JOIN product ON product.product_id = account.product_id " +
                "WHERE account.id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            account = map(rs);
            account.setCategory(rs.getString("category_name"));
            account.setProduct(rs.getString("product_name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(account);
    }

    @Override
    public void terminateAccount(long id) {
        String sql = "update account set status = ? where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "해지");
            pstmt.setLong(2, id);
            int count = pstmt.executeUpdate();
            if(count > 0) System.out.println("해지가 완료되었습니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBalance(long id, int price) {
        String sql = "update account set balance = balance + ? where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, price);
            pstmt.setLong(2, id);
            int count = pstmt.executeUpdate();
            if(count > 0) System.out.println("입금/출금이 완료되었습니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getBalance(long id) {
        int price = 0;
        String sql = "select balance from account where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            price = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return price;
    }

    private Account map(ResultSet rs) throws SQLException {
        return Account.builder()
                .id(rs.getLong("account_id"))
                .userId(rs.getLong("user_id"))
                .productId(rs.getLong("product_id"))
                .categoryId(rs.getLong("category_id"))
                .balance(rs.getInt("balance"))
                .createdAt(rs.getDate("created_at").toLocalDate())
                .status(rs.getString("status"))
                .accountNumber(rs.getString("account"))
                .build();

    }
}
