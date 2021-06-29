package com.egen.orderpickingservice.repository;

import com.egen.orderpickingservice.entity.BatchOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchOrderRepo extends JpaRepository<BatchOrder,Long> {
}
