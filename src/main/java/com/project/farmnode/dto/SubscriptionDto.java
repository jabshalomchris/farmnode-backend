package com.project.farmnode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long userId;
    private String fullName;
    private String username;
    private List<ProduceDto> priceDto = new ArrayList<>();

}
