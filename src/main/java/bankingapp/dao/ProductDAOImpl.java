package bankingapp.dao; // 패키지 이름 확인

import bankingapp.domain.ProductVO; // VO 클래스 import
import bankingapp.util.JDBCUtil;

import java.sql.*; // JDBC 관련 클래스들을 위한 import
import java.util.ArrayList; // ArrayList 클래스 import
import java.util.List; // List 인터페이스 import
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
// ---------------------------------------------

public class ProductDAOImpl implements ProductDAO { // ProductDAO 인터페이스 구현 확인
    // 생성자: 데이터베이스 연결을 초기화합니다.
    private final Connection conn = JDBCUtil.getConnection();

    // 테스트 데이터 삭제 메서드 (외래 키 제약 조건 검사 비활성화하여 강제 삭제)
    @Override
    public void deleteAllProducts() {
        System.out.println("[DEBUG-DEL] --- 테스트 데이터 삭제 시작 (FK 비활성화 방식) ---"); // 새로운 로그 시작
        // Statement 객체를 try-with-resources로 관리
        try {
            // 1. 외래 키 제약 조건 검사를 일시적으로 비활성화
            System.out.println("[DEBUG-DEL] 1/3. SET FOREIGN_KEY_CHECKS = 0; 시도...");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
                System.out.println("[DEBUG-DEL] -> SET FOREIGN_KEY_CHECKS = 0; 완료.");
            }

            // 2. product 테이블의 모든 데이터 삭제
            System.out.println("[DEBUG-DEL] 2/3. DELETE FROM product; 시도...");
            try (Statement stmt = conn.createStatement()) { // 새로운 Statement 객체 사용
                String deleteSql = "DELETE FROM product";
                int rowsAffected = stmt.executeUpdate(deleteSql);
                System.out.println("[DEBUG-DEL] -> product 테이블에서 " + rowsAffected + "개 행 삭제 완료.");
            }

            // 3. 외래 키 제약 조건 검사를 다시 활성화
            System.out.println("[DEBUG-DEL] 3/3. SET FOREIGN_KEY_CHECKS = 1; 시도...");
            try (Statement stmt = conn.createStatement()) { // 새로운 Statement 객체 사용
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
                System.out.println("[DEBUG-DEL] -> SET FOREIGN_KEY_CHECKS = 1; 완료.");
            }

            System.out.println("[DEBUG-DEL] --- 테스트 데이터 삭제 종료 ---");

        } catch (SQLException e) {
            // SQL 실행 중 예외 처리
            System.err.println("[ERROR-DEL] 테스트 데이터 삭제 중 오류 발생: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            // 예외를 다시 던져 테스트 중단
            throw new RuntimeException("테스트 데이터 삭제 실패", e);
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("[ERROR-DEL] 테스트 데이터 삭제 중 알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("테스트 데이터 삭제 중 알 수 없는 오류 발생", e);
        }
    }


    // --- ProductDAO 인터페이스 메서드 구현 (CRUD) ---

    @Override // 상품 삽입
    public void insertProduct(ProductVO product) {
        String sql = "INSERT INTO product (product_id, product_name, product_type, interest_rate, min_deposit, term, description, created_at, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println("[DEBUG-INSERT] insertProduct 시도 (ID: " + product.getProductId() + ")");
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getProductType());
            pstmt.setDouble(4, product.getInterestRate());
            pstmt.setDouble(5, product.getMinDeposit());
            pstmt.setString(6, product.getTerm());
            pstmt.setString(7, product.getDescription());
            pstmt.setTimestamp(8, product.getCreatedAt());
            pstmt.setString(9, product.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[DEBUG-INSERT] INSERT 결과: " + rowsAffected + "개 행 삽입 성공.");

        } catch (SQLException e) {
            System.err.println("[ERROR-INSERT] 상품 삽입 중 오류 발생 (ID: " + product.getProductId() + "): " + e.getMessage());
            e.printStackTrace();
            // 삽입 실패 시에도 테스트 흐름이 멈추지 않도록 여기서 예외를 다시 던지지는 않음
        }
        System.out.println("[DEBUG-INSERT] insertProduct 종료 (ID: " + product.getProductId() + ")");
    }

    @Override // 상품 수정
    public void updateProduct(ProductVO product) {
        String sql = "UPDATE product SET product_name = ?, product_type = ?, interest_rate = ?, min_deposit = ?, term = ?, description = ?, created_at = ?, status = ? WHERE product_id = ?";
        System.out.println("[DEBUG-UPDATE] updateProduct 시도 (ID: " + product.getProductId() + ")");
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getProductType());
            pstmt.setDouble(3, product.getInterestRate());
            pstmt.setDouble(4, product.getMinDeposit());
            pstmt.setString(5, product.getTerm());
            pstmt.setString(6, product.getDescription());
            pstmt.setTimestamp(7, product.getCreatedAt());
            pstmt.setString(8, product.getStatus());
            pstmt.setLong(9, product.getProductId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[DEBUG-UPDATE] UPDATE 결과: " + rowsAffected + "개 행 수정 완료.");

        } catch (SQLException e) {
            System.err.println("[ERROR-UPDATE] 상품 수정 중 오류 발생 (ID: " + product.getProductId() + "): " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[DEBUG-UPDATE] updateProduct 종료 (ID: " + product.getProductId() + ")");
    }

    @Override // 상품 삭제 (단건)
    public void deleteProduct(long productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        System.out.println("[DEBUG-DELETE] deleteProduct 시도 (ID: " + productId + ")");
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, productId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[DEBUG-DELETE] DELETE 결과: " + rowsAffected + "개 행 삭제 완료 (ID: " + productId + ").");

        } catch (SQLException e) {
            System.err.println("[ERROR-DELETE] 상품 삭제 중 오류 발생 (ID: " + productId + "): " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[DEBUG-DELETE] deleteProduct 종료 (ID: " + productId + ")");
    }

    @Override // 특정 상품 조회
    public ProductVO getProduct(long productId) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        System.out.println("[DEBUG-GET] getProduct 시도 (ID: " + productId + ")");
        ProductVO product = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new ProductVO(
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
            }
            System.out.println("[DEBUG-GET] GET 결과: 상품 " + (product != null ? "조회 성공" : "조회 실패") + " (ID: " + productId + ").");
        } catch (SQLException e) {
            System.err.println("[ERROR-GET] 상품 조회 중 오류 발생 (ID: " + productId + "): " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[DEBUG-GET] getProduct 종료 (ID: " + productId + ")");
        return product;
    }

    @Override // 전체 상품 조회
    public List<ProductVO> getAllProducts() {
        String sql = "SELECT * FROM product";
        System.out.println("[DEBUG-GETALL] getAllProducts 시도");
        List<ProductVO> productList = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
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
            System.out.println("[DEBUG-GETALL] GET ALL 결과: " + productList.size() + "개 상품 조회 완료.");

        } catch (SQLException e) {
            System.err.println("[ERROR-GETALL] 전체 상품 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[DEBUG-GETALL] getAllProducts 종료");
        return productList;
    }
}
