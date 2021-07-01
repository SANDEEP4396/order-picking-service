package com.egen.orderpickingservice.service;

import com.egen.orderpickingservice.dto.ItemDto;
import com.egen.orderpickingservice.dto.OrdersDto;
import com.egen.orderpickingservice.entity.Employee;
import com.egen.orderpickingservice.entity.Items;
import com.egen.orderpickingservice.entity.Orders;
import com.egen.orderpickingservice.enums.OrderStatus;
import com.egen.orderpickingservice.exceptions.EmployeeNotFoundException;
import com.egen.orderpickingservice.exceptions.OrderPickingServiceException;
import com.egen.orderpickingservice.mapper.OrdersMapper;
import com.egen.orderpickingservice.repository.EmployeeRepo;
import com.egen.orderpickingservice.repository.OrderPickingRepo;
import com.egen.orderpickingservice.service.OrderPickingService;
import com.egen.orderpickingservice.util.CalculatePerformance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    EmployeeRepo employeeRepo;

    OrdersMapper ordersMapper = new OrdersMapper();

    public OrderPickingServiceImpl(OrderPickingRepo orderPickingRepo,EmployeeRepo employeeRepo) {
        this.orderPickingRepo=orderPickingRepo;
        this.employeeRepo = employeeRepo;
    }

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
    public Boolean createOrders(List<OrdersDto> ordersDto){
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
        });
        orderPickingRepo.saveAll(totalOrdersList);
        return true;
    }

    public String getOrderPickStatus(Long id){
        Optional<Orders> orders = orderPickingRepo.findById(id);
        if(!orders.isPresent())
            throw new OrderPickingServiceException("Order status is not available for the given ID:"+id);
        else
          return orderPickingRepo.getOrderStatus(id);

    }

    public Boolean cancelPickedOrder(Long id){
        Optional<Orders> orders = orderPickingRepo.findById(id);
        if(!orders.isPresent())
            throw new OrderPickingServiceException("Order status is not available for the given ID:"+id);
        else {
            orders.get().setStatus(OrderStatus.CANCELLED);
            orderPickingRepo.save(orders.get());
            return true;
        }
    }

    public List<Orders> findAllByPageLimit(Integer pageNo, Integer pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);

        return Optional.ofNullable(orderPickingRepo.findAll(paging))
                .orElseThrow(() ->
                        new OrderPickingServiceException("No orders were found"))
                .getContent();
    }
    public List<Orders> sortByValues(Integer pageNo, Integer pageSize, String sortBy) {

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return Optional.ofNullable(orderPickingRepo.findAll(paging))
                .orElseThrow(() ->
                        new OrderPickingServiceException("No orders were found based on the sort values"))
                .getContent();
    }


    public String generatePerformanceReportofEmployee(long empId) {

        CalculatePerformance calculatePerformance = new CalculatePerformance();

        List<Orders> ordersList =  orderPickingRepo.findOrdersByEmployee_EmpId(empId);
        if(ordersList.isEmpty()){
            throw new EmployeeNotFoundException("There is no employee with Id: "+empId+" present");
        }
       HashMap<String,Double> averageTimePerOrder = calculatePerformance.averageTimePerOrder(ordersList);

       //Build a string output from the obtained hashmap values
       StringBuilder sb = new StringBuilder();
       sb.append("Average time taken by the employee: " +empId +" to complete"+
               " single order is: "+averageTimePerOrder.get("Single") +
               " batch orders is: "+averageTimePerOrder.get("Batch"));

        return sb.toString();
    }

    @Override
    public String getNumberOfPicksEachDay(Long empId, Timestamp startTime) {

        //Gets current date and adds 1 day to current date and sets it as end date/time
        Date tomorrow = new Date(startTime.getTime() + (1000 * 60 * 60 * 24));
        Timestamp ordersUpto = new Timestamp(tomorrow.getTime());
      List<Orders> totalOrders = Optional.ofNullable(orderPickingRepo
              .findAllByEmployee_EmpIdAndAndOrderEndTimeBetween(empId,startTime,
                ordersUpto))
                .orElseThrow(() ->
                        new OrderPickingServiceException("Orders between the specific time range not found" ));
      StringBuilder sb = new StringBuilder();
      sb.append("Total number of orders picked by the employee in the given time frame is: "+totalOrders.size());
        return sb.toString();
    }
}

