package com.egen.orderpickingservice.util;

import com.egen.orderpickingservice.entity.Orders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


public class CalculatePerformance {

    /**
     * This function calculates the average time taken by the employee to complete single order and batch order
     * @param orders
     * @return
     */
    public HashMap<String,Double> averageTimePerOrder(List<Orders> orders) {

        HashMap<String,Double> performance = new HashMap<>();
        Set<String> uniqueBatches = new HashSet<>();
        int totalOrders = orders.size();
        int totalBatchOrders;
        AtomicReference<Double> totalTime = new AtomicReference<>((double) 0);
        double averageTimePerOrder;
        double averageTimePerBatch;

    orders.forEach(order -> {
            Long timeDifference = order.getOrderEndTime().getTime() -order.getOrderStartTime().getTime();
            long diffMinutes = timeDifference / (60 * 1000) % 60;
            totalTime.updateAndGet(total -> new Double((double) (total + diffMinutes)));
            uniqueBatches.add(order.getBatchId());
    });

    //Calcuates average times for both single and batch orders
    totalBatchOrders = uniqueBatches.size();
    averageTimePerOrder = totalTime.get() / totalOrders;
    averageTimePerBatch = totalTime.get()/totalBatchOrders;

    performance.put("Single",averageTimePerOrder);
    performance.put("Batch",averageTimePerBatch);

    return performance;
    }
}
