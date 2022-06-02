package com.project.farmnode.dto;

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
    //measurement
    private String address;
    //geolocation
    private String measureType;
    private double longitude;
    private double latitude;
    private String publishStatus;
    private String userName;
    private String grower;
    private String filename;
    private int commentCount;
    private boolean subscription;
}
