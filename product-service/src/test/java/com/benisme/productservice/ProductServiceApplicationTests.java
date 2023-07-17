package com.benisme.productservice;

import com.benisme.productservice.dto.ProductRequest;
import com.benisme.productservice.model.Product;
import com.benisme.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0-focal");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.url",mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
    }

    @Test
    public void shouldCreateProductAndGetAllProducts() throws Exception {
        int numProducts = 5;

        for (int i = 0; i < numProducts; i++) {
            ProductRequest productRequest = createRandomProduct();
            String productRequestString = objectMapper.writeValueAsString(productRequest);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(productRequestString))
                    .andExpect(status().isCreated());
        }
        Assertions.assertEquals(numProducts, productRepository.findAll().size());
    }

    @Test
    public void shouldGetProductById() throws Exception {
        ProductRequest productRequest = createRandomProduct();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Product savedProduct = objectMapper.readValue(response, Product.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedProduct.getId()))
                .andExpect(jsonPath("$.name").value(savedProduct.getName()))
                .andExpect(jsonPath("$.description").value(savedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(savedProduct.getPrice().doubleValue()));
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        ProductRequest productRequest = createRandomProduct();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Product savedProduct = objectMapper.readValue(response, Product.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isNotFound());
    }


    private ProductRequest createRandomProduct() {
        Faker faker = new Faker();

        return ProductRequest.builder()
                .name(faker.commerce().productName())
                .description(faker.lorem().sentence())
                .price(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)))
                .build();
    }

}
