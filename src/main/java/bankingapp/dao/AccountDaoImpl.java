package bankingapp.dao;

import bankingapp.model.AccountVo;
import bankingapp.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    private final Connection conn = JDBCUtil.getConnection();

    @Override
    public void insert(AccountVo accountVo) {
        String sql = "insert into account values(?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, accountVo.getId());
            pstmt.setLong(2, accountVo.getUserId());
            pstmt.setLong(3, accountVo.getProductId());
            //pstmt.setLong(4, account.getCategoryId());
            pstmt.setInt(5, accountVo.getBalance());
            pstmt.setDate(6, Date.valueOf(accountVo.getCreatedAt()));
            pstmt.setString(7, accountVo.getStatus());
            pstmt.setString(8, accountVo.getAccountNumber());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AccountVo> getAccounts(long userId) {
        List<AccountVo> accountVos = new ArrayList<>();
        String sql = "SELECT * FROM account " +
                //"LEFT JOIN category ON category.category_id = account.category_id " +
                "LEFT JOIN product ON product.product_id = account.product_id " +
                "WHERE account.user_id = ?";
        //String sql = "select * from account where user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountVo accountVo = map(rs);
                //account.setCategory(rs.getString("category_name"));
                accountVo.setProduct(rs.getString("product_name"));
                accountVos.add(accountVo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountVos;
    }

    @Override
    public Optional<AccountVo> getAccount(long id) {
        AccountVo accountVo;
        //String sql = "select * from account where id = ?";
        String sql = "SELECT * FROM account " +
                //"LEFT JOIN category ON category.category_id = account.category_id " +
                "LEFT JOIN product ON product.product_id = account.product_id " +
                "WHERE account.id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            accountVo = map(rs);
            //account.setCategory(rs.getString("category_name"));
            accountVo.setProduct(rs.getString("product_name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(accountVo);
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

    @Override
    public void deleteAccount(long id) {
        String sql = "delete from account where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AccountVo map(ResultSet rs) throws SQLException {
        return AccountVo.builder()
                .id(rs.getLong("account_id"))
                .userId(rs.getLong("user_id"))
                .productId(rs.getLong("product_id"))
                //.categoryId(rs.getLong("category_id"))
                .balance(rs.getInt("balance"))
                .createdAt(rs.getDate("created_at").toLocalDate())
                .status(rs.getString("status"))
                .accountNumber(rs.getString("account"))
                .build();

    }
}
