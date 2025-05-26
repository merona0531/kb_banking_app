package bankingapp.dao; // 패키지 이름 확인

import bankingapp.domain.ProductVO; // VO 클래스 import

import java.sql.*; // JDBC 관련 클래스들을 위한 import
import java.util.ArrayList; // ArrayList 클래스 import
import java.util.List; // List 인터페이스 import

public class ProductDAOImpl implements ProductDAO { // ProductDAO 인터페이스 구현 확인
    private Connection connection;

    // 생성자: 데이터베이스 연결을 초기화합니다.
    public ProductDAOImpl() {
        // 데이터베이스 연결 설정에 필요한 정보 (세연님 실제 정보로 입력)
        String URL = "jdbc:mysql://localhost:3306/bank?serverTimezone=UTC&useSSL=false";
        String USER = "root";
        String PASSWORD = "1234";

        System.out.println("[DEBUG-CONN] ProductDAOImpl 생성자 시작"); // 생성자 시작 로그

        try {
            // DriverManager를 통해 데이터베이스에 연결 시도
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("[DEBUG-CONN] 데이터베이스에 성공적으로 연결되었습니다: " + URL); // 연결 성공 메시지 출력

        } catch (SQLException e) {
            // 연결 실패 시 예외 처리
            System.err.println("[ERROR-CONN] 데이터베이스 연결 실패: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace(); // 자세한 오류 정보 출력
            // 연결 실패 시 심각한 문제이므로 예외를 던져 프로그램 중단
            throw new RuntimeException("데이터베이스 연결 초기화 실패", e);
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("[ERROR-CONN] 데이터베이스 연결 중 알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 연결 초기화 중 알 수 없는 오류 발생", e);
        }
        System.out.println("[DEBUG-CONN] ProductDAOImpl 생성자 종료"); // 생성자 종료 로그
    }

    // 연결을 닫는 메서드 (자원 관리를 위해 필요 - 테스트 코드 마지막에 호출하면 좋음)
    public void closeConnection() {
        System.out.println("[DEBUG-CONN] 연결 종료 시도...");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DEBUG-CONN] 데이터베이스 연결이 종료되었습니다.");
            } else {
                System.out.println("[DEBUG-CONN] 데이터베이스 연결이 이미 닫혀있거나 null입니다.");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR-CONN] 데이터베이스 연결 종료 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 테스트 데이터 삭제 메서드 (외래 키 제약 조건 검사 비활성화하여 강제 삭제)
    @Override
    public void deleteAllProducts() {
        System.out.println("[DEBUG-DEL] --- 테스트 데이터 삭제 시작 (FK 비활성화 방식) ---"); // 새로운 로그 시작
        // Statement 객체를 try-with-resources로 관리
        try {
            // 1. 외래 키 제약 조건 검사를 일시적으로 비활성화
            System.out.println("[DEBUG-DEL] 1/3. SET FOREIGN_KEY_CHECKS = 0; 시도...");
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
                System.out.println("[DEBUG-DEL] -> SET FOREIGN_KEY_CHECKS = 0; 완료.");
            }

            // 2. product 테이블의 모든 데이터 삭제
            System.out.println("[DEBUG-DEL] 2/3. DELETE FROM product; 시도...");
            try (Statement stmt = connection.createStatement()) { // 새로운 Statement 객체 사용
                String deleteSql = "DELETE FROM product";
                int rowsAffected = stmt.executeUpdate(deleteSql);
                System.out.println("[DEBUG-DEL] -> product 테이블에서 " + rowsAffected + "개 행 삭제 완료.");
            }

            // 3. 외래 키 제약 조건 검사를 다시 활성화
            System.out.println("[DEBUG-DEL] 3/3. SET FOREIGN_KEY_CHECKS = 1; 시도...");
            try (Statement stmt = connection.createStatement()) { // 새로운 Statement 객체 사용
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
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
            System.out.println("[DEBUG-GETALL] GET ALL 결과: " + productList.size() + "개 상품 조회 완료.");

        } catch (SQLException e) {
            System.err.println("[ERROR-GETALL] 전체 상품 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[DEBUG-GETALL] getAllProducts 종료");
        return productList;
    }
}
