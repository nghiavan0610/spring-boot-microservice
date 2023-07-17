package com.benisme.productservice.service;

import com.benisme.productservice.dto.ProductRequest;
import com.benisme.productservice.dto.ProductResponse;
import com.benisme.productservice.exception.EntityNotFoundException;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(String id) throws EntityNotFoundException;

    boolean deleteProduct(String id) throws EntityNotFoundException;
}
