package com.project.farmnode.dto.ProduceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestItemsDto {

    private long produceId;
    private  String produceName;
    private  double price;
    private int quantity;
    private double linetotal;

}
