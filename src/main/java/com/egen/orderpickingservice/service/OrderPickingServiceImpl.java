package com.egen.orderpickingservice.service;

import com.egen.orderpickingservice.dto.ItemDto;
import com.egen.orderpickingservice.dto.OrdersDto;
import com.egen.orderpickingservice.entity.Employee;
import com.egen.orderpickingservice.entity.Items;
import com.egen.orderpickingservice.entity.Orders;
import com.egen.orderpickingservice.exceptions.OrderPickingServiceException;
import com.egen.orderpickingservice.mapper.OrdersMapper;
import com.egen.orderpickingservice.repository.EmployeeRepo;
import com.egen.orderpickingservice.repository.ItemsRepo;
import com.egen.orderpickingservice.repository.OrderPickingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@Slf4j
public class OrderPickingServiceImpl implements OrderPickingService {

    @Autowired
    OrderPickingRepo orderPickingRepo;

    @Autowired
    ItemsRepo itemsRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    OrdersMapper ordersMapper = new OrdersMapper();

    public List<Orders> getAllOrders() {
        return Optional.ofNullable(orderPickingRepo.findAll())
                .orElseThrow(() -> new OrderPickingServiceException("No orders are present currently"));
    }

    public Orders findOrderById(Long id) {
        return orderPickingRepo.findById(id)
                .orElseThrow(() ->
                        new OrderPickingServiceException("No orders with id: " + id + " were present in the inventory"));
    }

    /**
     * Checks if its a single order or batch order if its a batch order it will call
     * @createBatchOrders()
     * @param ordersDto - List of Orders send by the user
     * @return
     * @throws ParseException
     */
    public Boolean createOrders(List<OrdersDto> ordersDto) throws ParseException {

        boolean isBatchOrder = ordersDto.size() == 1 ? false : true;

        //Check if it's a batch order or not
        if (!isBatchOrder) {
            //Generate a batchId with Sequence "B" and set order Id and Batch Id
            Random random = new Random();
            Long orderNum = Long.valueOf(random.nextInt(100) + random.nextInt(10));
            StringBuilder sb = new StringBuilder();
            sb.append("B" + orderNum);

            Orders orders = ordersMapper.convertOrderDtoToEntity(ordersDto.get(0));
            orders.setBatchId(sb.toString());
            orders.setOrderId(orderNum);

            //Assign Employees in round robin manner
            List<Employee> employeeList = new ArrayList<>();
            employeeList = employeeRepo.findAll();
            orders.setEmployee(employeeList.get(0));

            List<Items> itemsList = new ArrayList<>(ordersDto.get(0).getItems().size());
            Iterator<ItemDto> it = ordersDto.get(0).getItems().iterator();
            while (it.hasNext()) {
                Items items = ordersMapper.convertItemDtoToEntity(it.next());
                items.setOrders(orders);
                itemsList.add(items);
            }
            orders.setOrderedItems(itemsList);
            orderPickingRepo.save(orders);
            return true;
        } else
            return createBatchOrders(ordersDto);
    }

    /**
     * Creates a batch of orders
     * @param ordersDto
     * @return
     */
    public Boolean createBatchOrders(List<OrdersDto> ordersDto) {
        List<Orders> totalOrdersList = new ArrayList<>(ordersDto.size());
        Random random = new Random();

        //Use of AtomicReference to increment OrderId inside lambda
        AtomicReference<Long> orderNum = new AtomicReference<>(Long.valueOf(random.nextInt(100)
                + random.nextInt(10)));
        StringBuilder sb = new StringBuilder();
        sb.append("B" + orderNum);

        List<Employee> employeeList = new ArrayList<>();
        employeeList = employeeRepo.findAll();
        List<Employee> finalEmployeeList = employeeList;
        AtomicInteger currEmp = new AtomicInteger();

        ordersDto.forEach(singleOrder -> {
            try {
                Orders orders = ordersMapper.convertOrderDtoToEntity(singleOrder);
                orders.setBatchId(sb.toString());
                orders.setOrderId(orderNum.get());

                //Assign Employees in round robin manner
                orders.setEmployee(finalEmployeeList.get(currEmp.getAndIncrement()));
                if (currEmp.get() > finalEmployeeList.size())
                    currEmp.set(0);

                orderNum.updateAndGet(inc -> inc + 1);

                List<Items> itemsList = new ArrayList<>(singleOrder.getItems().size());
                Iterator<ItemDto> it = singleOrder.getItems().iterator();
                while (it.hasNext()) {
                    Items items = ordersMapper.convertItemDtoToEntity(it.next());
                    items.setOrders(orders);
                    itemsList.add(items);
                }
                orders.setOrderedItems(itemsList);
                totalOrdersList.add(orders);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        orderPickingRepo.saveAll(totalOrdersList);
        return true;
    }
}

