package com.egen.orderpickingservice.controller;


import com.egen.orderpickingservice.dto.OrdersDto;
import com.egen.orderpickingservice.entity.Orders;
import com.egen.orderpickingservice.response.Response;
import com.egen.orderpickingservice.response.ResponseMetadata;
import com.egen.orderpickingservice.response.StatusMessage;
import com.egen.orderpickingservice.service.OrderPickingService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/order-picking")
@Api(description = "Order Picking related endpoints")
@Slf4j
public class OrderPickingController {

    @Autowired
    OrderPickingService orderPickingService;

    /**
     * Fetches all the orders
     * @return list of orders
     */
    @GetMapping(produces = "application/json")
    @ApiOperation(value = "Fetches all the orders",
            notes = "Returns all the orders")
    @ApiResponses(value={
            @ApiResponse(code=302,message = "FOUND"),
            @ApiResponse(code=500,message = "Interval Server Error"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<List<Orders>> getAllOrders(){
        return Response.<List<Orders>>builder()
                .meta(ResponseMetadata.builder().statusCode(302)
                        .statusMessage(StatusMessage.FOUND.name())
                        .build()).data((orderPickingService.getAllOrders()))
                .build();
    }

    /**
     * Fetches a particular order
     * @return list of orders
     */
    @GetMapping(value="/{id}", produces = "application/json")
    @ApiOperation(value = "Fetches a specific order",
            notes = "Returns a specific order")
    @ApiResponses(value={
            @ApiResponse(code=302,message = "FOUND"),
            @ApiResponse(code=500,message = "Interval Server Error"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<Orders> getOrderById(@ApiParam(value = "Order id of the order",required = true)
                                                   @PathVariable("id")Long id){
        return Response.<Orders>builder()
                .meta(ResponseMetadata.builder().statusCode(302)
                        .statusMessage(StatusMessage.FOUND.name()).build()).data((orderPickingService.findOrderById(id)))
                .build();
    }
    /**
     * Creates new order
     * @param ordersDto
     * @return
     */
    @PostMapping(value = "/place-order",consumes = "application/json",produces = "application/json")
    @ApiOperation(value = "Creates a new order",
            notes = "Send a list of orders")
    @ApiResponses(value={
            @ApiResponse(code=201,message = "CREATED"),
            @ApiResponse(code=500,message = "INTERNAL SERVER ERROR"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<String> placeOrder(@RequestBody List<OrdersDto> ordersDto) throws ParseException {
        return orderPickingService.createOrders(ordersDto) == Boolean.TRUE ? Response.<String>builder()
                .meta(ResponseMetadata.builder().statusCode(201)
                        .statusMessage(StatusMessage.CREATED.name())
                        .build())
                .data("Order placed successfully")
                .build()
                :
                Response.<String>builder()
                        .meta(ResponseMetadata.builder().statusCode(500)
                                .statusMessage(StatusMessage.UNKNOWN_INTERNAL_ERROR.name())
                                .build())
                        .data("Failed to place order")
                        .build();
    }
}
