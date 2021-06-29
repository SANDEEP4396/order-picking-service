package com.egen.orderpickingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long batchId;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Orders> orders;

}
