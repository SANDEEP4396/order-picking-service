package com.egen.orderpickingservice.controller;

import com.egen.orderpickingservice.response.Response;
import com.egen.orderpickingservice.response.ResponseMetadata;
import com.egen.orderpickingservice.response.StatusMessage;
import com.egen.orderpickingservice.service.OrderPickingService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order-picking/employee-performance")
@Api(description = "Monitor the performance of the Employee")
@Slf4j
public class EmployeePerformance {

    @Autowired
    OrderPickingService orderPickingService;

    @GetMapping(value="/{id}", produces = "application/json")
    @ApiOperation(value = "Calculates and displays the performance of an employee",
            notes = "Returns the performance report of a particular employee ")
    @ApiResponses(value={
            @ApiResponse(code=302,message = "FOUND"),
            @ApiResponse(code=500,message = "Interval Server Error"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<String> performanceReportofEmployee(@ApiParam(value = "Employee Id of the employee",required = true)
                                                            @PathVariable("id")Long empId){
        return Response.<String>builder()
                .meta(ResponseMetadata.builder().statusCode(302)
                        .statusMessage(StatusMessage.FOUND.name())
                        .build()).data((orderPickingService.generatePerformanceReportofEmployee(empId)))
                .build();
    }
}
