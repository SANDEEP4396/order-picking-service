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
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersDto implements Serializable {

    @JsonProperty(value = "store")
    private Store store;

    @JsonProperty(value = "items")
    private List<ItemDto> items;

    @JsonProperty(value = "isSubstituteAllowed")
    private Boolean isSubstituteAllowed;
}
