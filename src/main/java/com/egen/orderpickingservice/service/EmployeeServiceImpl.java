package com.egen.orderpickingservice.service;

import com.egen.orderpickingservice.dto.EmployeeDto;
import com.egen.orderpickingservice.entity.Employee;
import com.egen.orderpickingservice.repository.EmployeeRepo;
import com.egen.orderpickingservice.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepo employeeRepo;

    public Boolean createEmployee(EmployeeDto employeeDto) {
           employeeRepo.save(new Employee(employeeDto.getFirstName(),employeeDto.getLastName()));
        return true;
    }
}
