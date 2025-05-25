import bankingapp.dao.ProductDAO;
import bankingapp.dao.ProductDAOImpl;
import bankingapp.vo.ProductVO;
 // 적절한 패키지 이름으로 변경하세요

import bankingapp.vo.ProductVO;
import bankingapp.dao.ProductDAO;
import bankingapp.dao.ProductDAOImpl;

import java.sql.Timestamp; // Timestamp 클래스 import
import java.util.Date;     // 현재 시간을 얻기 위해 Date 클래스 활용 (java.util.Date)
import java.util.List;     // List 인터페이스 import

public class ProductDAOImplTest {

    public static void main(String[] args) {
        // ProductDAOImpl 객체 생성 - 이 객체를 통해 데이터베이스 작업 수행
        ProductDAO productDAO = new ProductDAOImpl();

        // --- 테스트를 위한 상품 데이터 생성 ---
        // product_id는 테이블 정의 상 AUTO_INCREMENT가 아니므로, 직접 고유한 값을 설정해야 합니다.
        long testProductId1 = 101L; // 첫 번째 테스트 상품 ID
        ProductVO product1 = new ProductVO();
        product1.setProductId(testProductId1);
        product1.setProductName("테스트 상품-예금");
        product1.setProductType("예금");
        product1.setInterestRate(4.0);
        product1.setMinDeposit(5000.0);
        product1.setTerm("6개월");
        product1.setDescription("테스트를 위한 단기 예금 상품입니다.");
        // 현재 시간을 Timestamp로 설정
        product1.setCreatedAt(new Timestamp(new Date().getTime()));
        product1.setStatus("Active");

        long testProductId2 = 102L; // 두 번째 테스트 상품 ID
        ProductVO product2 = new ProductVO();
        product2.setProductId(testProductId2);
        product2.setProductName("테스트 상품-적금");
        product2.setProductType("적금");
        product2.setInterestRate(5.2);
        product2.setMinDeposit(1000.0); // 적금은 최소 금액이 낮을 수 있습니다.
        product2.setTerm("24개월");
        product2.setDescription("테스트를 위한 장기 적금 상품입니다.");
        product2.setCreatedAt(new Timestamp(new Date().getTime())); // 현재 시간 설정
        product2.setStatus("Pending");


        // --- 1. 상품 추가 테스트 (insertProduct) ---
        System.out.println("--- 상품 추가 테스트 시작 ---");
        try {
            productDAO.insertProduct(product1);
            System.out.println("상품 추가 시도: " + product1.getProductName());
            productDAO.insertProduct(product2);
            System.out.println("상품 추가 시도: " + product2.getProductName());
            System.out.println("상품 추가 시도 완료 (DB에서 직접 확인 필요)");
        } catch (Exception e) {
            System.out.println("상품 추가 중 오류 발생: " + e.getMessage());
        }
        System.out.println("--- 상품 추가 테스트 종료 ---");
        System.out.println(); // 줄 바꿈


        // --- 2. 특정 상품 조회 테스트 (getProduct) ---
        System.out.println("--- 특정 상품 조회 테스트 시작 (ID: " + testProductId1 + ") ---");
        try {
            ProductVO retrievedProduct = productDAO.getProduct(testProductId1); // 첫 번째 테스트 상품 조회
            if (retrievedProduct != null) {
                System.out.println("조회된 상품 정보:");
                System.out.println("ID: " + retrievedProduct.getProductId());
                System.out.println("이름: " + retrievedProduct.getProductName());
                System.out.println("유형: " + retrievedProduct.getProductType());
                System.out.println("이자율: " + retrievedProduct.getInterestRate());
                System.out.println("최소금액: " + retrievedProduct.getMinDeposit());
                System.out.println("기간: " + retrievedProduct.getTerm());
                System.out.println("설명: " + retrievedProduct.getDescription());
                System.out.println("생성일: " + retrievedProduct.getCreatedAt());
                System.out.println("상태: " + retrievedProduct.getStatus());
            } else {
                System.out.println("상품 (ID: " + testProductId1 + ") 조회 실패 또는 존재하지 않음");
            }
        } catch (Exception e) {
            System.out.println("상품 조회 중 오류 발생: " + e.getMessage());
        }
        System.out.println("--- 특정 상품 조회 테스트 종료 ---");
        System.out.println(); // 줄 바꿈


        // --- 3. 전체 상품 목록 조회 테스트 (getAllProducts) ---
        System.out.println("--- 전체 상품 목록 조회 테스트 시작 ---");
        try {
            List<ProductVO> allProducts = productDAO.getAllProducts();
            System.out.println("전체 상품 목록 (" + allProducts.size() + "개):");
            if (allProducts.isEmpty()) {
                System.out.println("목록이 비어 있습니다.");
            } else {
                for (ProductVO product : allProducts) {
                    System.out.println("- ID: " + product.getProductId() + ", 이름: " + product.getProductName() + ", 유형: " + product.getProductType());
                }
            }
        } catch (Exception e) {
            System.out.println("전체 상품 조회 중 오류 발생: " + e.getMessage());
        }
        System.out.println("--- 전체 상품 목록 조회 테스트 종료 ---");
        System.out.println(); // 줄 바꿈


        // --- 4. 상품 수정 테스트 (updateProduct) ---
        // 위에서 조회한 상품이 있다면 수정 시도
        System.out.println("--- 상품 수정 테스트 시작 (ID: " + testProductId1 + ") ---");
        try {
            ProductVO productToUpdate = productDAO.getProduct(testProductId1); // 다시 조회하여 최신 상태 가져오기
            if (productToUpdate != null) {
                productToUpdate.setProductName("수정된 테스트 상품-예금"); // 이름 변경
                productToUpdate.setInterestRate(4.5); // 이자율 변경
                productDAO.updateProduct(productToUpdate);
                System.out.println("상품 수정 시도 완료 (ID: " + testProductId1 + ", DB에서 직접 확인 필요)");

                // 수정 후 다시 조회하여 확인 (선택 사항)
                // ProductVO updatedProduct = productDAO.getProduct(testProductId1);
                // System.out.println("수정 후 이름: " + updatedProduct.getProductName() + ", 이자율: " + updatedProduct.getInterestRate());

            } else {
                System.out.println("수정할 상품 (ID: " + testProductId1 + ")을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            System.out.println("상품 수정 중 오류 발생: " + e.getMessage());
        }
        System.out.println("--- 상품 수정 테스트 종료 ---");
        System.out.println(); // 줄 바꿈


        // --- 5. 상품 삭제 테스트 (deleteProduct) ---
        // 주의: 삭제는 실제 데이터를 지우므로 신중하게 실행하세요.
        // 테스트 데이터 정리 목적으로 사용합니다.
        System.out.println("--- 상품 삭제 테스트 시작 (ID: " + testProductId1 + ") ---");
        try {
            // productDAO.deleteProduct(testProductId1); // 첫 번째 테스트 상품 삭제 실행
            // System.out.println("상품 삭제 시도 완료 (ID: " + testProductId1 + ", DB에서 직접 확인 필요)");

            // productDAO.deleteProduct(testProductId2); // 두 번째 테스트 상품 삭제 실행
            // System.out.println("상품 삭제 시도 완료 (ID: " + testProductId2 + ", DB에서 직접 확인 필요)");

            System.out.println("상품 삭제 테스트 코드 주석 처리됨. 필요 시 주석을 해제하고 실행하세요.");

        } catch (Exception e) {
            System.out.println("상품 삭제 중 오류 발생: " + e.getMessage());
        }
        System.out.println("--- 상품 삭제 테스트 종료 ---");
        System.out.println(); // 줄 바꿈

        // --- 테스트 후 데이터베이스 연결 종료 (ProductDAOImpl에 close 메서드를 추가하는 것이 좋음) ---
        // 현재 ProductDAOImpl에는 연결을 닫는 메서드가 없으므로, 필요하다면 ProductDAO에 close() 메서드를 추가하고
        // 이 main 메서드 마지막에 호출하여 연결을 닫아주는 것이 자원을 효율적으로 관리하는 방법입니다.
        // try {
        //     if (productDAO instanceof ProductDAOImpl) {
        //         ((ProductDAOImpl) productDAO).closeConnection(); // ProductDAOImpl에 추가된 closeConnection 메서드 호출 예시
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
    }
}
