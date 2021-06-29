package com.egen.orderpickingservice.entity;

import com.egen.orderpickingservice.enums.Zone;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

   @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
   @JoinColumn(columnDefinition = "order_id")
   @JsonBackReference
    public Orders orders;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Long getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(Long wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public Long getSubstituteId() {
        return substituteId;
    }

    public void setSubstituteId(Long substituteId) {
        this.substituteId = substituteId;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
