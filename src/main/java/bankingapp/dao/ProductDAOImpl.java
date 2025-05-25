package bankingapp.dao;

import bankingapp.vo.ProductVO;
import bankingapp.dao.ProductDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    private Connection connection;

    public ProductDAOImpl() {
        // 데이터베이스 연결 설정
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/데이터베이스_이름", "사용자명", "비밀번호");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertProduct(ProductVO product) {
        String sql = "INSERT INTO product (product_id, product_name, product_type, interest_rate, min_deposit, term, description, created_at, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getProductType());
            pstmt.setDouble(4, product.getInterestRate());
            pstmt.setDouble(5, product.getMinDeposit());
            pstmt.setString(6, product.getTerm());
            pstmt.setString(7, product.getDescription());
            pstmt.setTimestamp(8, product.getCreatedAt());
            pstmt.setString(9, product.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(ProductVO product) {
        String sql = "UPDATE product SET product_name = ?, product_type = ?, interest_rate = ?, min_deposit = ?, term = ?, description = ?, created_at = ?, status = ? WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getProductType());
            pstmt.setDouble(3, product.getInterestRate());
            pstmt.setDouble(4, product.getMinDeposit());
            pstmt.setString(5, product.getTerm());
            pstmt.setString(6, product.getDescription());
            pstmt.setTimestamp(7, product.getCreatedAt());
            pstmt.setString(8, product.getStatus());
            pstmt.setLong(9, product.getProductId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(long productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductVO getProduct(long productId) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ProductVO(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_type"),
                        rs.getDouble("interest_rate"),
                        rs.getDouble("min_deposit"),
                        rs.getString("term"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductVO> getAllProducts() {
        List<ProductVO> productList = new ArrayList<>();
        String sql = "SELECT * FROM product";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProductVO product = new ProductVO(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_type"),
                        rs.getDouble("interest_rate"),
                        rs.getDouble("min_deposit"),
                        rs.getString("term"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getString("status")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}

