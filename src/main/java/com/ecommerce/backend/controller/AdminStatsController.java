package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.DashboardStats;
import com.ecommerce.backend.dto.TopProduct;
import com.ecommerce.backend.dto.RecentOrder;
import com.ecommerce.backend.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/stats")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminStatsController {

    private final OrderRepository orderRepo;

    public AdminStatsController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping
    public DashboardStats getStats() {
        long totalOrders = orderRepo.count();
        long pendingOrders = orderRepo.countByStatus("pending");

        long today = Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli();
        long since30d = Instant.now().minus(30, ChronoUnit.DAYS).toEpochMilli();

        double revenueToday = orderRepo.sumRevenueSince(today) != null ? orderRepo.sumRevenueSince(today) : 0;
        double revenue30d = orderRepo.sumRevenueSince(since30d) != null ? orderRepo.sumRevenueSince(since30d) : 0;

        List<TopProduct> topProducts = orderRepo.findTopProductsSince(since30d)
                .stream()
                .map(row -> new TopProduct((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());

        List<RecentOrder> recentOrders = orderRepo.findAll().stream()
                .sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
                .limit(5)
                .map(o -> new RecentOrder(o.getId(), o.getCreatedAt(), o.getStatus(), o.getTotal()))
                .collect(Collectors.toList());

        DashboardStats stats = new DashboardStats();
        stats.setTotalOrders(totalOrders);
        stats.setPendingOrders(pendingOrders);
        stats.setRevenueToday(revenueToday);
        stats.setRevenue30d(revenue30d);
        stats.setTopProducts30d(topProducts);
        stats.setRecentOrders(recentOrders);

        return stats;
    }
}
