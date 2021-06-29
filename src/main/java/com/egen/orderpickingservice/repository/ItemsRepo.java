package com.egen.orderpickingservice.repository;

import com.egen.orderpickingservice.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepo extends JpaRepository<Items,Long> {
}
