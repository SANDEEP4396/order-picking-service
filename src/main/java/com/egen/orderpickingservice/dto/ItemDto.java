package com.egen.orderpickingservice.dto;

import com.egen.orderpickingservice.enums.Zone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto implements Serializable {

    private Long id;

    @JsonProperty(value = "itemName")
    private String itemName;

    @JsonProperty(value = "itemQuantity")
    private int itemQuantity;

    @JsonProperty(value = "height")
    private double height;

    @JsonProperty(value = "weight")
    private double weight;

    @JsonProperty(value = "width")
    private double width;

    @JsonProperty(value = "length")
    private double length;

    @JsonProperty(value = "wareHouseId")
    private Long wareHouseId;

    @JsonProperty(value = "substituteId")
    private Long substituteId;

    @JsonProperty(value = "zone")
    private Zone zone;

}
