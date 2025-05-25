package bankingapp.test; // 패키지 이름 확인

import bankingapp.vo.ProductVO; // ProductVO 클래스 import
import bankingapp.dao.ProductDAO; // ProductDAO 인터페이스 import
import bankingapp.dao.ProductDAOImpl; // ProductDAOImpl 클래스 import

import java.sql.Timestamp; // Timestamp 클래스 import
import java.util.Date;     // 현재 시간을 얻기 위해 Date 클래스 활용 (java.util.Date)
import java.util.List;     // List 인터페이스 import

public class ProductDAOImplTest {

    public static void main(String[] args) {
        System.out.println("[DEBUG-MAIN] --- Test Main Method Started ---"); // Main 시작 로그

        ProductDAO productDAO = null; // DAO 객체를 미리 null로 초기화

        try {
            // DAO 구현체 객체 생성 (생성자에서 DB 연결 시도)
            System.out.println("[DEBUG-MAIN] ProductDAOImpl 객체 생성 시도...");
            productDAO = new ProductDAOImpl();
            System.out.println("[DEBUG-MAIN] ProductDAOImpl 객체 생성 완료.");

            // --- 테스트 시작 전 기존 데이터 삭제 (Duplicate Entry 오류 방지) ---
            System.out.println("[DEBUG-MAIN] --- Starting Test Data Deletion ---"); // 삭제 시작 전 로그
            if (productDAO != null) {
                // DAO 객체가 성공적으로 생성되었으면 삭제 메서드 호출
                productDAO.deleteAllProducts();
            } else {
                System.err.println("[ERROR-MAIN] ProductDAOImpl 객체가 null이라 삭제 테스트 건너뜀.");
            }
            System.out.println("[DEBUG-MAIN] --- Finished Test Data Deletion ---");
            System.out.println(); // Newline

            // --- 테스트를 위한 상품 데이터 생성 ---
            System.out.println("[DEBUG-MAIN] 테스트 상품 VO 객체 생성...");
            long testProductId1 = 101L; // 첫 번째 테스트 상품 ID
            ProductVO product1 = new ProductVO();
            product1.setProductId(testProductId1);
            product1.setProductName("테스트 상품-예금");
            product1.setProductType("예금");
            product1.setInterestRate(4.0);
            product1.setMinDeposit(5000.0);
            product1.setTerm("6개월");
            product1.setDescription("테스트를 위한 단기 예금 상품입니다.");
            product1.setCreatedAt(new Timestamp(new Date().getTime())); // 현재 시간 설정
            product1.setStatus("Active");

            long testProductId2 = 102L; // 두 번째 테스트 상품 ID
            ProductVO product2 = new ProductVO();
            product2.setProductId(testProductId2);
            product2.setProductName("테스트 상품-적금");
            product2.setProductType("적금");
            product2.setInterestRate(5.2);
            product2.setMinDeposit(1000.0);
            product2.setTerm("24개월");
            product2.setDescription("테스트를 위한 장기 적금 상품입니다.");
            product2.setCreatedAt(new Timestamp(new Date().getTime())); // 현재 시간 설정
            product2.setStatus("Pending");
            System.out.println("[DEBUG-MAIN] 테스트 상품 VO 객체 생성 완료.");
            System.out.println();


            // --- 1. 상품 추가 테스트 (insertProduct) ---
            System.out.println("[DEBUG-MAIN] --- Starting Product Insertion Test ---");
            try { // 삽입 테스트는 오류 나더라도 다음 테스트 진행 위해 try-catch로 감쌈
                if (productDAO != null) {
                    productDAO.insertProduct(product1); // 첫 번째 상품 삽입 시도
                    productDAO.insertProduct(product2); // 두 번째 상품 삽입 시도
                } else {
                    System.err.println("[ERROR-MAIN] ProductDAOImpl 객체가 null이라 삽입 테스트 건너뜀.");
                }
                // 콘솔 메시지는 insertProduct 메서드 안에서 출력되므로 여기서는 추가 메시지 최소화
            } catch (Exception e) { // 혹시 insertProduct 외에서 발생하는 예외도 잡음
                System.err.println("[ERROR-MAIN] 상품 추가 테스트 중 예상치 못한 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println("[DEBUG-MAIN] --- Finished Product Insertion Test ---");
            System.out.println(); // 줄 바꿈


            // --- 2. 특정 상품 조회 테스트 (getProduct) ---
            System.out.println("[DEBUG-MAIN] --- Starting Specific Product Query Test (ID: " + testProductId1 + ") ---");
            if (productDAO != null) {
                ProductVO retrievedProduct = productDAO.getProduct(testProductId1); // 추가한 상품 ID로 조회
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
                    System.out.println("상품 (ID: " + testProductId1 + ") 조회 결과: 상품을 찾을 수 없습니다.");
                }
            } else {
                System.err.println("[ERROR-MAIN] ProductDAOImpl 객체가 null이라 조회 테스트 건너뜀.");
            }
            System.out.println("[DEBUG-MAIN] --- Finished Specific Product Query Test ---");
            System.out.println(); // 줄 바꿈


            // --- 3. 전체 상품 목록 조회 테스트 (getAllProducts) ---
            System.out.println("[DEBUG-MAIN] --- Starting All Products Query Test ---");
            if (productDAO != null) {
                List<ProductVO> allProducts = productDAO.getAllProducts();
                System.out.println("전체 상품 목록 (" + allProducts.size() + "개):");
                if (allProducts.isEmpty()) {
                    System.out.println("-> 목록이 비어 있습니다.");
                } else {
                    for (ProductVO product : allProducts) {
                        System.out.println("-> ID: " + product.getProductId() + ", 이름: " + product.getProductName() + ", 유형: " + product.getProductType());
                    }
                }
            } else {
                System.err.println("[ERROR-MAIN] ProductDAOImpl 객체가 null이라 전체 조회 테스트 건너뜀.");
            }
            System.out.println("[DEBUG-MAIN] --- Finished All Products Query Test ---");
            System.out.println(); // 줄 바꿈


            // --- 4. 상품 수정 테스트 (updateProduct) ---
            System.out.println("[DEBUG-MAIN] --- Starting Product Update Test (ID: " + testProductId1 + ") ---");
            if (productDAO != null) {
                // 수정할 상품을 다시 조회하여 최신 상태 가져오기
                ProductVO productToUpdate = productDAO.getProduct(testProductId1);
                if (productToUpdate != null) {
                    System.out.println("[DEBUG-MAIN] 수정 대상 상품 조회 성공: " + productToUpdate.getProductName());
                    productToUpdate.setProductName("수정된 테스트 상품-예금"); // 이름 변경
                    productToUpdate.setInterestRate(4.5); // 이자율 변경
                    // 다른 필드도 필요하다면 수정
                    // productToUpdate.setStatus("Inactive");

                    productDAO.updateProduct(productToUpdate); // 수정 메서드 호출

                    // 수정 후 다시 조회하여 확인 (선택 사항)
                    // System.out.println("[DEBUG-MAIN] 수정 후 상품 다시 조회 시도...");
                    // ProductVO updatedProduct = productDAO.getProduct(testProductId1);
                    // if (updatedProduct != null) {
                    //     System.out.println("[DEBUG-MAIN] 수정 후 이름 확인: " + updatedProduct.getProductName() + ", 이자율: " + updatedProduct.getInterestRate());
                    // }

                } else {
                    System.err.println("[ERROR-MAIN] 수정할 상품 (ID: " + testProductId1 + ")을 찾을 수 없습니다.");
                }
            } else {
                System.err.println("[ERROR-MAIN] ProductDAOImpl 객체가 null이라 수정 테스트 건너뜀.");
            }
            System.out.println("[DEBUG-MAIN] --- Finished Product Update Test ---");
            System.out.println(); // 줄 바꿈


            // --- 5. 상품 삭제 테스트 (deleteProduct) ---
            // 주의: 삭제는 실제 데이터를 지우므로 신중하게 실행하세요.
            // 테스트 데이터 정리 목적으로 사용합니다.
            System.out.println("[DEBUG-MAIN] --- Starting Product Deletion Test (ID: " + testProductId1 + ") ---");
            // 이 부분의 주석을 해제하고 실행하면 ID 101 상품을 삭제합니다.
            // if (productDAO != null) {
            //    System.out.println("[DEBUG-MAIN] deleteProduct 시도 (ID: " + testProductId1 + ")...");
            //    productDAO.deleteProduct(testProductId1); // 첫 번째 테스트 상품 삭제 실행
            //    System.out.println("[DEBUG-MAIN] deleteProduct 시도 완료 (ID: " + testProductId1 + "). DB에서 직접 확인 필요.");
            // } else {
            //     System.err.println("[ERROR-MAIN] ProductDAOImpl 객체가 null이라 삭제 테스트 건너뜀.");
            // }
            System.out.println("상품 삭제 테스트 코드 주석 처리됨. 필요 시 주석을 해제하고 실행하세요."); // 주석 해제 전에는 이 메시지가 출력됨
            System.out.println("[DEBUG-MAIN] --- Finished Product Deletion Test ---");
            System.out.println(); // 줄 바꿈

        } catch (RuntimeException e) {
            // ProductDAOImpl 생성자나 deleteAllProducts에서 던져진 RuntimeException을 여기서 잡음
            System.err.println("[FATAL ERROR] 테스트 초기 설정 또는 삭제 중 치명적인 오류 발생: " + e.getMessage());
            e.printStackTrace();
            // 치명적인 오류 발생 시 프로그램 종료
            System.exit(1);
        } catch (Exception e) {
            // 그 외 예상치 못한 다른 예외 처리
            System.err.println("[FATAL ERROR] 테스트 실행 중 예상치 못한 오류 발생: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            // --- 테스트 후 데이터베이스 연결 종료 ---
            System.out.println("[DEBUG-MAIN] 테스트 완료. 데이터베이스 연결 종료 시도...");
            if (productDAO instanceof ProductDAOImpl) {
                ((ProductDAOImpl) productDAO).closeConnection(); // ProductDAOImpl의 연결 종료 메서드 호출
            } else {
                System.err.println("[ERROR-MAIN] DAO 객체가 ProductDAOImpl 인스턴스가 아니라 연결 종료 건너뜀.");
            }
            System.out.println("[DEBUG-MAIN] 데이터베이스 연결 종료 시도 완료.");
        }


        System.out.println("[DEBUG-MAIN] --- Test Main Method Finished ---"); // Main 종료 로그
    }
}
