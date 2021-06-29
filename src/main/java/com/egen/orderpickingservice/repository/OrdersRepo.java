package com.egen.orderpickingservice.repository;

import com.egen.orderpickingservice.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepo extends JpaRepository<Orders,Long> {
}
