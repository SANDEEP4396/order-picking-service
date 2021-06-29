package com.egen.orderpickingservice.repository;

import com.egen.orderpickingservice.entity.EmployeePerformance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeePerformanceRepo extends JpaRepository<EmployeePerformance,Long> {
}
