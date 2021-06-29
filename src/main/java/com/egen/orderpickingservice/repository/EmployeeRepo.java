package com.egen.orderpickingservice.repository;

import com.egen.orderpickingservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee,Long> {
}
