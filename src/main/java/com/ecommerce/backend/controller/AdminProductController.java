package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.ProductRepository;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = {"http://localhost:5173", "https://ecommerce-frontend-lemon-five-79.vercel.app"})
public class AdminProductController {

    private final ProductRepository repo;

    public AdminProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getAll() {
        return repo.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product create(@RequestBody Product product) {
        return repo.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return repo.save(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
