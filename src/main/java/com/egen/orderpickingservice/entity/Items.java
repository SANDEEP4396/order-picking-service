package com.egen.orderpickingservice.entity;

import com.egen.orderpickingservice.enums.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long itemId;

    private String itemName;

    private int itemQuantity;

    private double height;

    private double weight;

    private double width;

    private double length;

    private Long wareHouseId;

    private Long substituteId;

    @Enumerated(EnumType.STRING)
    private Zone zone;

}
