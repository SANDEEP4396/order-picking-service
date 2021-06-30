package com.egen.orderpickingservice.mapper;

import com.egen.orderpickingservice.dto.ItemDto;
import com.egen.orderpickingservice.dto.OrdersDto;
import com.egen.orderpickingservice.entity.Items;
import com.egen.orderpickingservice.entity.Orders;
import com.egen.orderpickingservice.enums.OrderStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;

import java.util.Random;

public class OrdersMapper {


    ModelMapper modelMapper = new ModelMapper();
    ObjectMapper mapper = new ObjectMapper();

    public Orders convertOrderDtoToEntity(OrdersDto ordersDto) {

        Orders orders = modelMapper.map(ordersDto,Orders.class);
        Date date = new Date();
        Timestamp date_ordered = new Timestamp(date.getTime());
        orders.setDateOrdered(date_ordered);
        orders.setOrderEndTime(date_ordered);
        orders.setStatus(OrderStatus.PICKED);

        //Set start time to few minutes before the endtime
        Timestamp timestamp = orders.getOrderEndTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        Random randomMinute = new Random();

        // subtract a random minute from current time
        cal.add(Calendar.MINUTE, -randomMinute.nextInt(59));
        orders.setOrderStartTime(new Timestamp(cal.getTime().getTime()));

        return orders;
    }

    public Items convertItemDtoToEntity(ItemDto itemDto){
        Items items = modelMapper.map(itemDto,Items.class);
        return items;
    }
}
