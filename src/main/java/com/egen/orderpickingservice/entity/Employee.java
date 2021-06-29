package com.egen.orderpickingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private Long empId;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "orderId")
    private  Orders orderId;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name="empPerformanceId")
    private List<EmployeePerformance> performanceId;
}
