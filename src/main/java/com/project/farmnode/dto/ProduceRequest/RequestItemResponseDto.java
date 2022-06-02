package com.project.farmnode.dto.ProduceRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestItemResponseDto {

    private long produceId;
    private  String produceName;
    private  double price;
    private int quantity;
    private double linetotal;
}
