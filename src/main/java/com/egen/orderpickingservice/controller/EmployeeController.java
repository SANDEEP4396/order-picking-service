package com.egen.orderpickingservice.controller;

import com.egen.orderpickingservice.dto.EmployeeDto;
import com.egen.orderpickingservice.response.Response;
import com.egen.orderpickingservice.response.ResponseMetadata;
import com.egen.orderpickingservice.response.StatusMessage;
import com.egen.orderpickingservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * This function is used to create a new employee
     * @return - returns HTTP status as created and details of the new customer
     */
    @PostMapping(value="/registration",consumes = "application/json",produces = "application/json")
    public Response<Boolean> createEmployee(@RequestBody EmployeeDto employeeDto){
        return employeeService.createEmployee(employeeDto) == Boolean.TRUE ?
                Response.<Boolean>builder().meta(ResponseMetadata.builder().statusCode(201)
                        .statusMessage(StatusMessage.CREATED.name())
                        .build()).data(true).build()
                :
                Response.<Boolean>builder().meta(ResponseMetadata.builder().statusCode(409)
                        .statusMessage(StatusMessage.CONFLICT.name())
                        .build()).data(false).build();
    }
}
