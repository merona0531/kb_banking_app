package bankingapp.dao;

import bankingapp.vo.ProductVO;

import java.util.List;

public interface ProductDAO {
    void insertProduct(ProductVO product);
    void updateProduct(ProductVO product);
    void deleteProduct(long productId);
    ProductVO getProduct(long productId);
    List<ProductVO> getAllProducts();
}

