package com.egen.orderpickingservice.service;

import com.egen.orderpickingservice.dto.OrdersDto;
import com.egen.orderpickingservice.entity.Orders;

import java.text.ParseException;
import java.util.List;

public interface OrderPickingService {
    List<Orders> getAllOrders();
    Orders findOrderById(Long id);

    Boolean createOrders(List<OrdersDto> ordersDto) throws ParseException;
}
