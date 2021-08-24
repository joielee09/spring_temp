package com.carbonzero.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carbonzero.domain.Product;
import com.carbonzero.dto.ProductResponseData;
import com.carbonzero.error.ProductNotFoundException;
import com.carbonzero.repository.ProductRepository;
import com.github.dozermapper.core.Mapper;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    private final Mapper mapper;
    private final ProductRepository productRepository;

    public ProductServiceImpl(Mapper mapper, ProductRepository productRepository) {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    /**
     * 상품을 생성한다.
     * @param product
     * @return 생성된 상품
     */
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * 상품 목록을 반환한다.
     * @return 상품 목록
     */
    @Override
    public Page<ProductResponseData> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return products.map((product) -> mapper.map(product, ProductResponseData.class));
    }

    /**
     * 특정 상품을 반환한다.
     * @param id 상품 아이디
     * @return 조회할 상품
     */
    @Override
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     *  상품을 업데이트한다.
     * @param id,productRequestData
     * @return int
     */
    @Override
    public Product updateProduct(Long id, Product source) {
        Product selectedProduct = findProduct(id);
        selectedProduct.changeWith(source);
        return selectedProduct;
    }

    /**
     * 상품을 삭제한다.
     * @param id 삭제할 상품의 아이디
     */
    @Override
    public void deleteProduct(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    /**
     * 특정한 상품을 조회하여 존재한다면 반환한다.
     * @param id 검색할 상품 아이디
     * @return 상품
     */
    public Product findProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
