package com.project.farmnode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduceFilterDto {
    private String sw_lat;
    private String ne_lat;
    private String sw_lng;
    private String ne_lng;
}
