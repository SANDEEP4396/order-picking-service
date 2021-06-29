package com.egen.orderpickingservice.entity;

import com.egen.orderpickingservice.enums.OrderStatus;
import com.egen.orderpickingservice.enums.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long orderId;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name="empId")
    private EmployeePerformance employeePerformance;

    private Timestamp dateOrdered;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private Store store;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Items> items;

    private Boolean isSubstituteAllowed;

}
