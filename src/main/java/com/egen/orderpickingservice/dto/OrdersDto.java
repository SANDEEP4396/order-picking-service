package com.egen.orderpickingservice.dto;

import com.egen.orderpickingservice.entity.Items;
import com.egen.orderpickingservice.enums.OrderStatus;
import com.egen.orderpickingservice.enums.Store;
import com.egen.orderpickingservice.enums.Zone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersDto implements Serializable {
    Date date = new Date();

    private Long id;

    private Timestamp dateOrdered = new Timestamp(date.getTime());

    private OrderStatus orderStatus = OrderStatus.PLACED;

    @JsonProperty(value = "store")
    private Store store;

    @JsonProperty(value = "items")
    private Set<Items> items;

    @JsonProperty(value = "isSubstituteAllowed")
    private Boolean isSubstituteAllowed;

    @JsonProperty(value = "empId")
    private Long empId;

}
