package bankingapp.vo;

import java.sql.Timestamp;

public class ProductVO {
    // 데이터베이스 테이블의 컬럼과 매핑되는 필드들
    private long productId; // 상품 고유 ID (BIGINT)
    private String productName; // 상품명 (VARCHAR(10))
    private String productType; // 상품 유형 (VARCHAR(20))
    private double interestRate; // 이자율 (DECIMAL(5,2)) -> double 또는 BigDecimal 사용 가능
    private double minDeposit; // 최소 가입 금액 (DECIMAL) -> double 또는 BigDecimal 사용 가능
    private String term; // 계약 기간 (VARCHAR(20))
    private String description; // 상품 설명 (VARCHAR(255))
    private Timestamp createdAt; // 생성일 (DATETIME)
    private String status; // 상태 (VARCHAR(10))

    // 기본 생성자
    public ProductVO() {
    }

    // 모든 필드를 초기화하는 생성자
    public ProductVO(long productId, String productName, String productType, double interestRate, double minDeposit, String term, String description, Timestamp createdAt, String status) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.interestRate = interestRate;
        this.minDeposit = minDeposit;
        this.term = term;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
    }

    // --- Getter와 Setter 메서드 ---

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMinDeposit() {
        return minDeposit;
    }

    public void setMinDeposit(double minDeposit) {
        this.minDeposit = minDeposit;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // (선택 사항) 객체 정보 출력을 위한 toString() 메서드
    @Override
    public String toString() {
        return "ProductVO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productType='" + productType + '\'' +
                ", interestRate=" + interestRate +
                ", minDeposit=" + minDeposit +
                ", term='" + term + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                '}';
    }
}


