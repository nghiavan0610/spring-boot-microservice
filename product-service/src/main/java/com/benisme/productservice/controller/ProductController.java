package com.benisme.productservice.controller;

import com.benisme.productservice.dto.ProductRequest;
import com.benisme.productservice.dto.ProductResponse;
import com.benisme.productservice.exception.EntityNotFoundException;
import com.benisme.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable String id) throws EntityNotFoundException {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> deleteProduct(@PathVariable String id) throws EntityNotFoundException {
        boolean deleted = productService.deleteProduct(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", deleted);
        return response;
    }
}
