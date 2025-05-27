package bankingapp.view;

import bankingapp.dao.ProductDAO;
import bankingapp.domain.ProductVO;

import java.util.List;
import java.util.Scanner;
import java.sql.Timestamp; // Timestamp 사용

public class ProductView {
    private ProductDAO productDAO;
    public ProductView(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // 상품 관리 메인 메뉴
    public void showProductMenu(Scanner scanner) {
        while (true) {
            printProductMenu();
            int choice = getUserInput(scanner);

            switch (choice) {
                case 1:
                    listProducts(scanner); // 상품 목록 조회 화면으로 이동
                    break;
                case 2:
                    registerNewProduct(scanner); // 신규 상품 등록 화면으로 이동
                    break;
                case 0:
                    System.out.println("🔙 메인 메뉴로 돌아갑니다.");
                    return; // 메인 메뉴로 돌아가기
                default:
                    System.out.println("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    private void printProductMenu() {
        System.out.println("\n--- 🛍️ 상품 관리 메뉴 ---");
        System.out.println("1. 상품 목록 조회");
        System.out.println("2. 신규 상품 등록");
        System.out.println("0. 메인 메뉴로 돌아가기");
        System.out.print("메뉴 선택: ");
    }

    private int getUserInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("❗️ 숫자만 입력해주세요.");
            scanner.next(); // 잘못된 입력 버퍼 비우기
            System.out.print("메뉴 선택: ");
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // 숫자 입력 후 남은 개행문자 처리
        return input;
    }
    // ### 4-1. 상품 목록 조회 화면 ###
    public void listProducts(Scanner scanner) {
        printListProducts();

        // 상품 선택 또는 뒤로가기 옵션 제공
        while (true) {
            System.out.println("\n옵션: [상품 ID 입력] 상세 정보 조회 / [0] 뒤로가기");
            System.out.print("입력: ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                return; // 뒤로가기
            }

            try {
                long productId = Long.parseLong(input);
                // 해당 ID의 상품이 실제로 존재하는지 확인 후 상세 화면으로 이동
                ProductVO selectedProduct = productDAO.getProduct(productId);
                if (selectedProduct != null) {
                    showProductDetail(scanner, selectedProduct); // 상품 상세 정보 화면으로 이동
                } else {
                    System.out.println("❗️ 해당 상품 ID를 가진 상품이 없습니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❗️ 올바른 상품 ID 또는 옵션을 입력해주세요.");
            }
        }
    }

    public void printListProducts() {
        System.out.println("\n--- 🛍️ 상품 목록 조회 ---");

        // TODO: 검색 및 필터 기능 구현 필요 (지금은 전체 목록만 출력)
        List<ProductVO> products = productDAO.getAllProducts(); // DAO 메서드 호출

        if (products.isEmpty()) {
            System.out.println("🔍 등록된 상품이 없습니다.");
        } else {
            // 테이블 형태로 깔끔하게 출력
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s | %-15s | %-10s | %-8s | %-10s | %-10s | %-10s%n",
                    "ID", "상품명", "유형", "이율", "최소금액", "계약기간", "상태");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            for (ProductVO product : products) {
                System.out.printf("%-5d | %-15s | %-10s | %-8.2f | %-10.2f | %-10s | %-10s%n",
                        product.getProductId(),
                        product.getProductName(),
                        product.getProductType(),
                        product.getInterestRate(),
                        product.getMinDeposit(),
                        product.getTerm(),
                        product.getStatus());
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
        }
    }

    // ### 4-2. 상품 상세 정보 화면 ###
    public void showProductDetail(Scanner scanner, ProductVO product) {
        System.out.println("\n--- 🛍️ 상품 상세 정보 ---");
        System.out.println("상품 ID: " + product.getProductId());
        System.out.println("상품명: " + product.getProductName());
        System.out.println("상품 유형: " + product.getProductType());
        System.out.println("기본 이율: " + product.getInterestRate());
        System.out.println("최소 가입 금액: " + product.getMinDeposit());
        System.out.println("계약 기간: " + product.getTerm());
        System.out.println("상품 설명: " + product.getDescription());
        System.out.println("등록일: " + product.getCreatedAt());
        System.out.println("판매 상태: " + product.getStatus());

        // TODO: 권한 체크 로직 필요
        while (true) {
            System.out.println("\n옵션: [1] 수정 / [2] 삭제 / [0] 뒤로가기");
            System.out.print("선택: ");
            int choice = getUserInput(scanner);

            switch (choice) {
                case 1:
                    editProduct(scanner, product); // 상품 정보 수정 화면으로 이동
                    // 수정 완료 후 상세 정보 다시 로드 (또는 수정된 객체로 업데이트)
                    product = productDAO.getProduct(product.getProductId()); // 최신 정보 다시 가져오기
                    if (product == null) return; // 상품이 삭제되었을 경우 상세 화면 종료
                    showProductDetail(scanner, product); // 수정 후 다시 상세 정보 표시
                    return; // 수정 후 상세 정보 표시했으니 현재 상세 화면 메서드 종료
                case 2:
                    deleteProduct(scanner, product.getProductId()); // 상품 삭제 확인 화면으로 이동
                    // 삭제 완료 후 목록 화면으로 돌아가야 함
                    return; // 삭제 완료 또는 취소 후 상세 화면 종료
                case 0:
                    return; // 뒤로가기 (상품 목록 화면으로 돌아감)
                default:
                    System.out.println("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    // ### 4-3. 신규 상품 등록 화면 ###
    public void registerNewProduct(Scanner scanner) {
        System.out.println("\n--- 🛍️ 신규 상품 등록 ---");
        ProductVO newProduct = new ProductVO();

        // TODO: 상품 ID 자동 생성 (DB의 AUTO_INCREMENT 활용) 또는 사용자 입력 받기
        // 현재 테이블 정의는 AUTO_INCREMENT가 아니므로 사용자 입력을 받거나,
        // DB에서 마지막 ID를 조회하여 +1 하거나, UUID 등을 사용해야 함.
        // 여기서는 간단하게 사용자에게 ID를 입력받는 것으로 예시합니다.
        System.out.print("상품 ID (숫자): ");
        long productId = getUserLongInput(scanner); // Long 타입 입력 받기
        newProduct.setProductId(productId);

        System.out.print("상품명: ");
        newProduct.setProductName(scanner.nextLine());

        System.out.print("상품 유형 (예: 예금, 적금): ");
        newProduct.setProductType(scanner.nextLine());

        System.out.print("기본 이율 (소수점 가능): ");
        newProduct.setInterestRate(getUserDoubleInput(scanner));

        System.out.print("최소 가입 금액: ");
        newProduct.setMinDeposit(getUserDoubleInput(scanner));

        System.out.print("계약 기간 (예: 6개월, 1년): ");
        newProduct.setTerm(scanner.nextLine());

        System.out.print("상품 설명: ");
        newProduct.setDescription(scanner.nextLine());

        // TODO: 판매 상태 드롭다운 -> 콘솔에서는 직접 입력 받기
        System.out.print("판매 상태 (예: Active, Inactive): ");
        newProduct.setStatus(scanner.nextLine());

        newProduct.setCreatedAt(new Timestamp(System.currentTimeMillis())); // 현재 시간으로 등록일 자동 설정

        System.out.println("\n입력 정보 확인:");
        System.out.println(newProduct.toString()); // ProductVO의 toString() 메서드 활용

        System.out.print("이대로 등록하시겠습니까? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            // TODO: 입력 값 유효성 검사 필요
            productDAO.insertProduct(newProduct); // DAO 메서드 호출
            // TODO: DAO 메서드에서 성공/실패 여부를 반환하도록 수정하면 더 좋음
            System.out.println("✅ 상품이 등록되었습니다.");
        } else {
            System.out.println("❌ 상품 등록을 취소했습니다.");
        }
    }

    // ### 4-4. 상품 정보 수정 화면 ###
    public void editProduct(Scanner scanner, ProductVO productToEdit) {
        System.out.println("\n--- 🛍️ 상품 정보 수정 (ID: " + productToEdit.getProductId() + ") ---");
        System.out.println("수정할 항목의 현재 값:");
        System.out.println("상품명: " + productToEdit.getProductName());
        System.out.println("상품 유형: " + productToEdit.getProductType());
        System.out.println("기본 이율: " + productToEdit.getInterestRate());
        System.out.println("최소 가입 금액: " + productToEdit.getMinDeposit());
        System.out.println("계약 기간: " + productToEdit.getTerm());
        System.out.println("상품 설명: " + productToEdit.getDescription());
        System.out.println("판매 상태: " + productToEdit.getStatus());
        System.out.println("⚠️ 'Enter' 키를 누르면 기존 값을 유지합니다.");

        System.out.print("상품명 (현재: " + productToEdit.getProductName() + "): ");
        String productName = scanner.nextLine();
        if (!productName.isEmpty()) productToEdit.setProductName(productName);

        System.out.print("상품 유형 (현재: " + productToEdit.getProductType() + "): ");
        String productType = scanner.nextLine();
        if (!productType.isEmpty()) productToEdit.setProductType(productType);

        System.out.print("기본 이율 (현재: " + productToEdit.getInterestRate() + "): ");
        String interestRateStr = scanner.nextLine();
        if (!interestRateStr.isEmpty()) {
            try { productToEdit.setInterestRate(Double.parseDouble(interestRateStr)); }
            catch (NumberFormatException e) { System.out.println("❗️ 잘못된 이율 형식입니다. 기존 값 유지."); }
        }

        System.out.print("최소 가입 금액 (현재: " + productToEdit.getMinDeposit() + "): ");
        String minDepositStr = scanner.nextLine();
        if (!minDepositStr.isEmpty()) {
            try { productToEdit.setMinDeposit(Double.parseDouble(minDepositStr)); }
            catch (NumberFormatException e) { System.out.println("❗️ 잘못된 금액 형식입니다. 기존 값 유지."); }
        }


        System.out.print("계약 기간 (현재: " + productToEdit.getTerm() + "): ");
        String term = scanner.nextLine();
        if (!term.isEmpty()) productToEdit.setTerm(term);

        System.out.print("상품 설명 (현재: " + productToEdit.getDescription() + "): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) productToEdit.setDescription(description);

        System.out.print("판매 상태 (현재: " + productToEdit.getStatus() + "): ");
        String status = scanner.nextLine();
        if (!status.isEmpty()) productToEdit.setStatus(status);

        // 등록일은 보통 수정하지 않음

        System.out.println("\n수정된 정보 확인:");
        System.out.println(productToEdit.toString());

        System.out.print("이대로 수정하시겠습니까? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            // TODO: 입력 값 유효성 검사 필요
            productDAO.updateProduct(productToEdit); // DAO 메서드 호출
            // TODO: DAO 메서드에서 성공/실패 여부를 반환하도록 수정하면 더 좋음
            System.out.println("✅ 상품 정보가 수정되었습니다.");
        } else {
            System.out.println("❌ 상품 정보 수정을 취소했습니다.");
        }
    }

    // ### 4-5. 상품 삭제 확인 화면 ###
    public void deleteProduct(Scanner scanner, long productId) {
        ProductVO productToDelete = productDAO.getProduct(productId); // 삭제 대상 상품 정보 가져오기

        if (productToDelete == null) {
            System.out.println("❗️ 삭제할 상품 (ID: " + productId + ")을 찾을 수 없습니다.");
            return; // 상품 없으면 삭제 절차 중단
        }

        System.out.println("\n--- 🛍️ 상품 삭제 확인 ---");
        System.out.println("삭제 대상 상품: [ID: " + productToDelete.getProductId() + "] " + productToDelete.getProductName());
        System.out.print("정말 삭제하시겠습니까? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            // TODO: 외래 키 제약 조건으로 인해 삭제가 실패할 수 있습니다.
            // 실제 시스템에서는 관련 데이터를 먼저 삭제하거나, 외래 키 설정을 변경해야 합니다.
            // 테스트 환경에서는 deleteAllProducts처럼 외래 키 검사 비활성화를 고려할 수도 있습니다.
            productDAO.deleteProduct(productId); // DAO 메서드 호출
            // TODO: DAO 메서드에서 성공/실패 여부를 반환하도록 수정하면 더 좋음
            System.out.println("✅ 상품이 삭제되었습니다.");
        } else {
            System.out.println("❌ 상품 삭제를 취소했습니다.");
        }
    }

    // 숫자 입력을 안전하게 받는 헬퍼 메서드
    private int getUserIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("❗️ 올바른 숫자 형식을 입력해주세요.");
            scanner.next(); // 잘못된 입력 버퍼 비우기
            System.out.print("입력: ");
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // 숫자 입력 후 남은 개행문자 처리
        return input;
    }

    // Long 타입 입력을 안전하게 받는 헬퍼 메서드
    private long getUserLongInput(Scanner scanner) {
        while (!scanner.hasNextLong()) {
            System.out.println("❗️ 올바른 숫자(ID) 형식을 입력해주세요.");
            scanner.next(); // 잘못된 입력 버퍼 비우기
            System.out.print("입력: ");
        }
        long input = scanner.nextLong();
        scanner.nextLine(); // 숫자 입력 후 남은 개행문자 처리
        return input;
    }

    // Double 타입 입력을 안전하게 받는 헬퍼 메서드
    private double getUserDoubleInput(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("❗️ 올바른 숫자(소수점 포함 가능) 형식을 입력해주세요.");
            scanner.next(); // 잘못된 입력 버퍼 비우기
            System.out.print("입력: ");
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // 숫자 입력 후 남은 개행문자 처리
        return input;
    }
}

