package com.egen.orderpickingservice.repository;

import com.egen.orderpickingservice.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPickingRepo extends JpaRepository<Orders,Long> {
}
