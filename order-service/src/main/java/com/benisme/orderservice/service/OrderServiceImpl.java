package com.benisme.orderservice.service;

import com.benisme.orderservice.dto.InventoryResponse;
import com.benisme.orderservice.dto.OrderLineItemsDto;
import com.benisme.orderservice.dto.OrderRequest;
import com.benisme.orderservice.model.Order;
import com.benisme.orderservice.model.OrderLineItems;
import com.benisme.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient.Builder webClientBuilder, ObservationRegistry observationRegistry) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call Inventory Service, and place order if product is in stock
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup", this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");

        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponseArr = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/v1/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductIsInStock = Arrays.stream(inventoryResponseArr)
                    .allMatch(InventoryResponse::isInStock);

            if(allProductIsInStock) {
                orderRepository.save(order);
                return "Order Placed Successfully";
            } else {
                throw new IllegalArgumentException("Product is out of stock, please try again later");
            }
        });

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
