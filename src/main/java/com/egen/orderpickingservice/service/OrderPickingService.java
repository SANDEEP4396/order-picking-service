package com.egen.orderpickingservice.service;

import com.egen.orderpickingservice.dto.OrdersDto;
import com.egen.orderpickingservice.entity.Orders;

import java.util.List;

public interface OrderPickingService {
    List<Orders> getAllOrders();
    Orders findOrderById(Long id);
    Boolean createOrders(List<OrdersDto> ordersDto);
    String getOrderPickStatus(Long id);
    Boolean cancelPickedOrder(Long id);
    List<Orders> findAllByPageLimit(Integer pageNo, Integer pageSize);
    List<Orders> sortByValues(Integer pageNo, Integer pageSize, String sortBy);

    String generatePerformanceReportofEmployee(long empId);
}
