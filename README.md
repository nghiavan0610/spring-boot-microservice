# spring-boot-microservices

This project demonstrates a Spring Boot-based microservices architecture with a primary focus on the "Order Products" functionality.

<ul>
    <li>
        <b>Product Service</b> 
        <br>
        <p>The Product Service is responsible for managing product-related operations. It provides APIs to retrieve product information, add new products, update existing ones, and delete products.</p>
        <br>
    </li>
    <li>
        <b>Inventory Service</b> 
        <br>
        <p>The Inventory Service is in charge of handling inventory-related tasks. It exposes APIs to track available product quantities, restock products, and update inventory information. The service is utilized by the Order Service to verify whether products are in stock or not.</p>
        <br>
    </li>
    <li>
        <b>Order Service</b> 
        <br>
        <p>The Order Service deals with order management. It allows users to place new orders, view existing orders, and cancel orders if necessary. The service interacts with the Inventory Service to verify the availability of products before accepting orders.</p>
        <br>
    </li>
</ul>

## Key Features

<ul>
<li>Service Discovery: Netflix Eureka</li>
<li>API Gateway: Spring Cloud Gateway</li>
<li>Security: Keycloak</li>
<li>Circuit Breaker: Resilience4j</li>
<li>Distributed Tracing: Zipkin</li>
<li>Event Driven Architecture: Kafka</li>
<li>Dockerized Application</li>
<li>Monitoring: Prometheus and Grafana</li>
</ul>
