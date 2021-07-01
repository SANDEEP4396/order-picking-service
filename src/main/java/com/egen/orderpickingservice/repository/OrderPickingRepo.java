package com.egen.orderpickingservice.repository;

import com.egen.orderpickingservice.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderPickingRepo extends JpaRepository<Orders,Long> {
    Page<Orders> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT status FROM Orders ord WHERE ord.order_id =:id ")
    String getOrderStatus(Long id);

    List<Orders> findOrdersByEmployee_EmpId(Long empId);

    List<Orders> findAllByEmployee_EmpIdAndAndOrderEndTimeBetween(Long empId, Timestamp startTime, Timestamp endTime);
}
