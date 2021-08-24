package com.carbonzero.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.carbonzero.domain.Product;
import com.carbonzero.dto.ProductRequestData;
import com.carbonzero.dto.ProductResponseData;
import com.carbonzero.dto.ProductSearchRequest;
import com.carbonzero.service.ProductSearchService;
import com.carbonzero.service.ProductServiceImpl;
import com.github.dozermapper.core.Mapper;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final Mapper mapper;
    private final ProductServiceImpl productServiceImpl;
    private final ProductSearchService productSearchService;
    private final PagedResourcesAssembler<ProductResponseData> assembler;

    public ProductController(Mapper mapper, ProductServiceImpl productServiceImpl, ProductSearchService productSearchService,
        PagedResourcesAssembler<ProductResponseData> assembler) {
        this.mapper = mapper;
        this.productServiceImpl = productServiceImpl;
        this.productSearchService = productSearchService;
        this.assembler = assembler;
    }

    /**
     * 상품 생성을 요청한다.
     * @param productRequestData
     * @return 생성된 상품 정보
     */
    @PostMapping
    public ResponseEntity<ProductResponseData> create(@RequestBody @Valid ProductRequestData productRequestData) {

        Product product = mapper.map(productRequestData, Product.class);

        Product createdProduct = productServiceImpl.createProduct(product);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdProduct.getId())
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        ProductResponseData response = mapper.map(createdProduct, ProductResponseData.class);

        return ResponseEntity
            .created(location)
            .body(response);
    }

    /**
     * 상품 목록 조회를 요청한다.
     * @return 상품 리스트
     */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponseData>>> list(
        @PageableDefault(size = 20, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProductResponseData> products = productServiceImpl.
            getProducts(pageable);

        // HATEOAS
        PagedModel<EntityModel<ProductResponseData>> entityModels = assembler.toModel(products);

        return ResponseEntity
            .ok()
            .body(entityModels);
    }

    /**
     * 특정 상품을 조회한다.
     * @param id 상품 아이디
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<ProductResponseData> detail(@PathVariable Long id) {
        Product product = productServiceImpl.getProduct(id);

        ProductResponseData response = mapper.map(product, ProductResponseData.class);

        return ResponseEntity
            .ok()
            .body(response);
    }

    /**
     * 상품 검색
     * @param productSearchRequest
     * @return
     */
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody ProductSearchRequest productSearchRequest) {
        return ResponseEntity.ok().body(productSearchService.search(productSearchRequest));
    }

    /**
     * 상품 정보를 업데이트한다.
     * @param id, productRequestData
     * @return 업데이트 결과
     */
    @PutMapping("{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody @Valid ProductRequestData productRequestData) {
        Product product = mapper.map(productRequestData, Product.class);
        Product updatedProduct = productServiceImpl.updateProduct(id, product);
        return ResponseEntity
                .ok()
                .body(updatedProduct);
    }

    /**
     * 상품을 삭제한다.
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        productServiceImpl.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
