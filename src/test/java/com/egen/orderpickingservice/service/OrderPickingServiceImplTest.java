package com.egen.orderpickingservice.service;

import com.egen.orderpickingservice.dto.ItemDto;
import com.egen.orderpickingservice.dto.OrdersDto;
import com.egen.orderpickingservice.entity.Employee;
import com.egen.orderpickingservice.entity.Items;
import com.egen.orderpickingservice.entity.Orders;
import com.egen.orderpickingservice.enums.OrderStatus;
import com.egen.orderpickingservice.enums.Store;
import com.egen.orderpickingservice.enums.Zone;
import com.egen.orderpickingservice.exceptions.EmployeeNotFoundException;
import com.egen.orderpickingservice.exceptions.OrderPickingServiceException;
import com.egen.orderpickingservice.repository.EmployeeRepo;
import com.egen.orderpickingservice.repository.OrderPickingRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderPickingServiceImplTest {

    @Mock
    private OrderPickingRepo orderPickingRepo;

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    OrderPickingService orderPickingService = new OrderPickingServiceImpl(orderPickingRepo,employeeRepo);

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllOrders() {
        when(orderPickingRepo.findAll()).thenReturn(returnListofOrders());
        List<Orders> ordersList = orderPickingService.getAllOrders();
        assertEquals(2,ordersList.size());
    }

    @Test
    void getAllOrdersFailure() {
        when(orderPickingRepo.findAll()).thenReturn(null);
        assertThrows(OrderPickingServiceException.class, () -> orderPickingService.getAllOrders());
    }

    @Test
    void findOrderById() {
        Orders orders = new Orders();
        orders.setOrderId(8L);
        orders.setEmployee(createEmployee().get(0));
        orders.setBatchId("B101");
        when(orderPickingRepo.findById(8L)).thenReturn(Optional.of(orders));
        Orders returnedOrder = orderPickingService.findOrderById(8L);
        assertEquals("B101",returnedOrder.getBatchId());
        assertEquals(8L,returnedOrder.getOrderId());
        assertEquals(1L,returnedOrder.getEmployee().getEmpId());
    }

    @Test
    void getOrderByIdFailure() {
        assertThrows(OrderPickingServiceException.class, () -> orderPickingService.findOrderById(8L));
    }
    @Test
    void createOrders() {
        when(employeeRepo.findAll()).thenReturn(createEmployee());
        Boolean result = orderPickingService.createOrders(createSingleOrderDto());
        Assertions.assertTrue(result);
    }

    @Test
    void createBatchOrders() {
        when(employeeRepo.findAll()).thenReturn(createEmployee());
        Boolean result = orderPickingService.createBatchOrders(createBatchOrderDto());
        Assertions.assertTrue(result);
    }

    @Test
    void getOrderPickStatus() {
        Orders orders = new Orders();
        orders.setOrderId(8L);
        orders.setStatus(OrderStatus.PICKED);
        when(orderPickingRepo.findById(8L)).thenReturn(Optional.of(orders));
        Orders returnedOrder = orderPickingService.findOrderById(8L);
        assertEquals("PICKED",returnedOrder.getStatus().toString());
    }

    @Test
    void failedToGetOrderPickStatus(){
        assertThrows(OrderPickingServiceException.class,()->orderPickingService.getOrderPickStatus(8L));
    }

    @Test
    void cancelPickedOrder() {
        Orders orders = new Orders();
        orders.setOrderId(8L);
        orders.setStatus(OrderStatus.CANCELLED);
        when(orderPickingRepo.findById(8L)).thenReturn(Optional.of(orders));
        assertTrue(orderPickingService.cancelPickedOrder(8L));
    }
    @Test
    void failedToCancelPickedOrder(){
        assertThrows(OrderPickingServiceException.class,()->orderPickingService.cancelPickedOrder(8L));
    }

    @Test
    void generatePerformanceReportofEmployee() {
        when(orderPickingRepo.findOrdersByEmployee_EmpId(1L)).thenReturn(returnListofOrders());
        String result = orderPickingService.generatePerformanceReportofEmployee(1L);
        assertEquals("Average time taken by the employee: 1 to complete single order is: 0.0 batch orders is: 0.0"
                ,result);
    }

    @Test
    void noEmployeesFoundToGenereatePerformanceReport(){
        assertThrows(EmployeeNotFoundException.class,()->
                orderPickingService.generatePerformanceReportofEmployee(1L));
    }

    @Test
    void findAllByPageLimit(){
        List<Orders> orders = new ArrayList<>();
        Page<Orders> pagedTasks = new PageImpl<>(orders);
        when(orderPickingRepo.findAll(org.mockito.Matchers.isA(Pageable.class))).thenReturn(pagedTasks);
        List<Orders> ordersList = orderPickingService.findAllByPageLimit(0,1);
        assertEquals(0,ordersList.size());

    }
    @Test
    void findAllByPageLimitFailed(){
        assertThrows(OrderPickingServiceException.class,()->orderPickingService.findAllByPageLimit(0,1));
    }

    List<Orders> returnListofOrders(){
        Items items = new Items();
        items.setItemName("Tv stand");
        items.setItemQuantity(1); items.setHeight(2.0);items.setLength(5.0);
        items.setWeight(20.0);items.setSubstituteId(12L);items.setWareHouseId(12L);
        items.setZone(Zone.FURNITURE);items.setWidth(2.59);

        List<Items> itemList = new ArrayList<>();
        itemList.add(items);

        Orders firstOrder = new Orders();
        firstOrder.setStore(Store.COSTCO);
        firstOrder.setIsSubstituteAllowed(true);
        firstOrder.setOrderedItems(itemList);
        firstOrder.setOrderStartTime(Timestamp.valueOf("2021-06-30 01:37:15"));
        firstOrder.setOrderEndTime(Timestamp.valueOf("2021-06-30 01:37:15"));

        Orders secondOrder = new Orders();
        secondOrder.setStore(Store.COSTCO);
        secondOrder.setIsSubstituteAllowed(true);
        secondOrder.setOrderedItems(itemList);
        secondOrder.setOrderStartTime(Timestamp.valueOf("2021-06-30 01:37:15"));
        secondOrder.setOrderEndTime(Timestamp.valueOf("2021-06-30 01:37:15"));

        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(firstOrder);
        ordersList.add(secondOrder);
        return ordersList;
    }
    List<Employee> createEmployee(){
        List<Employee> employeeList = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setFirstName("Sandeep");
        employee1.setLastName("Sagar");
        employee1.setEmpId(1L);
        Employee employee2 = new Employee();
        employee2.setFirstName("Sandeep");
        employee2.setLastName("Sagar");
        employee2.setEmpId(1L);
        Employee employee3 = new Employee();
        employee3.setFirstName("Sandeep");
        employee3.setLastName("Sagar");
        employee3.setEmpId(1L);
        employeeList.add(employee1);
        employeeList.add(employee2);
        employeeList.add(employee3);
        return employeeList;
    }
    List<OrdersDto> createSingleOrderDto(){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemName("Tv stand");
        itemDto.setItemQuantity(1); itemDto.setHeight(2.0);itemDto.setLength(5.0);
        itemDto.setWeight(20.0);itemDto.setSubstituteId(12L);itemDto.setWareHouseId(12L);
        itemDto.setZone(Zone.FURNITURE);itemDto.setWidth(2.59);

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setStore(Store.COSTCO);
        ordersDto.setIsSubstituteAllowed(true);
        ordersDto.setItems(itemDtoList);

        List<OrdersDto> ordersDtoList = new ArrayList<>();
        ordersDtoList.add(ordersDto);
        return ordersDtoList;

    }
    List<OrdersDto> createBatchOrderDto(){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemName("Tv stand");
        itemDto.setItemQuantity(1); itemDto.setHeight(2.0);itemDto.setLength(5.0);
        itemDto.setWeight(20.0);itemDto.setSubstituteId(12L);itemDto.setWareHouseId(12L);
        itemDto.setZone(Zone.FURNITURE);itemDto.setWidth(2.59);

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        OrdersDto firstOrder = new OrdersDto();
        firstOrder.setStore(Store.COSTCO);
        firstOrder.setIsSubstituteAllowed(true);
        firstOrder.setItems(itemDtoList);

        OrdersDto secondOrder = new OrdersDto();
        secondOrder.setStore(Store.COSTCO);
        secondOrder.setIsSubstituteAllowed(true);
        secondOrder.setItems(itemDtoList);

        List<OrdersDto> ordersDtoList = new ArrayList<>();
        ordersDtoList.add(firstOrder);
        ordersDtoList.add(secondOrder);
        return ordersDtoList;

    }

}