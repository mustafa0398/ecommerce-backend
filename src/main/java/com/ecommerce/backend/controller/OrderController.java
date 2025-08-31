package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.OrderItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderRepository repo;
    private final ProductRepository productRepo;

    public OrderController(OrderRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    @GetMapping("/{userId}")
    public List<Order> getOrders(@PathVariable String userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        order.setCreatedAt(System.currentTimeMillis());
        order.setStatus("pending");

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);

                if (item.getProduct() != null && item.getProduct().getId() != null) {
                    Product product = productRepo.findById(item.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProduct().getId()));
                    item.setProduct(product);
                } else {
                    throw new RuntimeException("OrderItem must contain a product with valid id");
                }
            }
        }

        return repo.save(order);
    }
}
