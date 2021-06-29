package com.egen.orderpickingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long empPerformanceId;

    private Timestamp empPerStartDate;

    private Timestamp empPerEndDate;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "empId")
    private Employee employee;
}
