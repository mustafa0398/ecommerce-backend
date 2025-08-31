package com.ecommerce.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.ProductRepository;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ProductRepository repo) {
        return args -> {
            if (repo.count() == 0) { 
                repo.save(new Product(null, "Laptop", 99999, "Ein schneller Laptop", "Elektronik", 
                    "https://www.cnet.com/a/img/resize/bb8a2aa9c31f8ec08d82228a51eabf05f00e54d2/hub/2025/03/10/d190e21d-9634-440d-8f33-396c8cb3da6a/m4-macbook-air-15-11.jpg?auto=webp&height=500"));
                repo.save(new Product(null, "Kopfhörer", 1999, "Noise Cancelling", "Audio", 
                    "https://blaupunkt.com/wp-content/uploads/2023/11/PSD_HPB_30_1-1024x1024.jpg"));
            }
        };
    }

}
