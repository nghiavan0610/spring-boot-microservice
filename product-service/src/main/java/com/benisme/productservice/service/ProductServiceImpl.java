package com.benisme.productservice.service;

import com.benisme.productservice.dto.ProductRequest;
import com.benisme.productservice.dto.ProductResponse;
import com.benisme.productservice.exception.EntityNotFoundException;
import com.benisme.productservice.model.Product;
import com.benisme.productservice.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        productRepository.save(product);
        return mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    @Override
    public ProductResponse getProductById(String id) throws EntityNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new EntityNotFoundException(Product.class, "id", id));
        return mapToProductResponse(product);
    }

    @Override
    public boolean deleteProduct(String id) throws EntityNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new EntityNotFoundException(Product.class, "id", id));
        productRepository.delete(product);
        return true;
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
