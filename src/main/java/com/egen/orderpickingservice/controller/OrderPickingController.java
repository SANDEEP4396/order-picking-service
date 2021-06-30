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
@Api(description = "Order Picking Service related endpoints")
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
     * @param id - Order id
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

    /**
     * This API will cancel the picked order
     * @param id - Order id
     * @return
     */
    @PutMapping(value = "/cancel-picked/{id}",produces = "application/json")
    @ApiOperation(value = "Cancels the picked order",
            notes = "Changes the status to cancelled")
    @ApiResponses(value={
            @ApiResponse(code=500,message = "INTERNAL SERVER ERROR"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<String>  cancelOrder(@ApiParam(value = "Order id of the order",required = true)
                                             @PathVariable("id")Long id){
        return orderPickingService.cancelPickedOrder(id) == Boolean.TRUE ? Response.<String>builder()
                .meta(ResponseMetadata.builder().statusCode(200)
                        .statusMessage(StatusMessage.SUCCESS.name())
                        .build())
                .data("Picked order has been cancelled. We apologies for not keeping up to your expectations ")
                .build()
                :
                Response.<String>builder()
                        .meta(ResponseMetadata.builder().statusCode(500)
                                .statusMessage(StatusMessage.UNKNOWN_INTERNAL_ERROR.name())
                                .build())
                        .data("Failed to cancel the picked order")
                        .build();
    }

    /**
     * Fetches the Order status of a specific order which is picked
     * @param id - Order id
     * @return List of orders
     */
    @GetMapping(value="order-status/{id}", produces = "application/json")
    @ApiOperation(value = "Fetches the status of the given order",
            notes = "Specify the order_id")
    @ApiResponses(value={
            @ApiResponse(code=302,message = "FOUND"),
            @ApiResponse(code=404,message = "Not Found"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<String> getOrderStatus(@ApiParam(value = "OrderId of the order",required = true)
                                                                 @PathVariable("id")Long id){
        return Response.<String>builder()
                .meta(ResponseMetadata.builder().statusCode(302)
                        .statusMessage(StatusMessage.FOUND.name())
                        .build()).data((orderPickingService.getOrderPickStatus(id)))
                .build();
    }

    /**
     * Sorts the order by the desired field
     * @param pageNo - set the page number -default 0
     * @param pageSize - set the page size -default -10
     * @param sortBy - set the sort by field - default - itemQuantity
     * @return
     */
    @GetMapping(produces = "application/json",value = "/sortby")
    @ApiOperation(value = "Sorts the value according to the value selected by the user",
            notes = "Provide the value which you want to sort")
    @ApiResponses(value={
            @ApiResponse(code=500,message = "INTERNAL SERVER ERROR"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<List<Orders>> getSortedValues(@RequestParam(defaultValue = "0")Integer pageNo,
                                                  @RequestParam(defaultValue = "10")Integer pageSize,
                                                  @RequestParam(defaultValue = "dateOrdered")String sortBy){
        return Response.<List<Orders>>builder()
                .meta(ResponseMetadata.builder().statusCode(302)
                        .statusMessage(StatusMessage.FOUND.name())
                        .build()).data((orderPickingService.sortByValues(pageNo,pageSize,sortBy)))
                .build();
    }

    /**
     * Fetches all the orders and limits the page size
     * @param pageNo - set the page number -default 0
     * @param pageSize - set the page size -default -10
     * @return
     */
    @GetMapping(produces = "application/json",value = "/pagelimit")
    @ApiOperation(value = "Fetches all the orders by limiting 10 orders per page by default",
            notes = "Returns 10 orders in one page and can be changed to other values as well")
    @ApiResponses(value={
            @ApiResponse(code=302,message = "FOUND"),
            @ApiResponse(code=500,message = "Interval Server Error"),
            @ApiResponse(code=200,message = "OK")
    })
    public Response<List<Orders>> getAllOrdersByPageLimit(@RequestParam(defaultValue = "0")Integer pageNo,
                                                          @RequestParam(defaultValue = "10")Integer pageSize){
        return Response.<List<Orders>>builder()
                .meta(ResponseMetadata.builder().statusCode(302)
                        .statusMessage(StatusMessage.FOUND.name())
                        .build()).data((orderPickingService.findAllByPageLimit(pageNo,pageSize)))
                .build();
    }

}
