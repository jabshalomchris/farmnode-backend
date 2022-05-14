package com.project.farmnode.dto;

import com.project.farmnode.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduceDto {
    private Long produceId;
    private String produceName;
    private String description;
    private String produceStatus;
    private double price;
    //category
    private String category;
    //measurment
    private String address;
    //geolocation
    private double longitude;
    private double latitude;
    private String publishStatus;
    private String userName;
}
