package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data 
@AllArgsConstructor 
@NoArgsConstructor  
public class DashboardStats {
    private long totalOrders;
    private long pendingOrders;
    private double revenueToday;
    private double revenue30d;
    private List<TopProduct> topProducts30d;
    private List<RecentOrder> recentOrders;
}
