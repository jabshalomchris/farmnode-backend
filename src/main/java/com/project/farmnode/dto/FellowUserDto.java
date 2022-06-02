package com.project.farmnode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FellowUserDto {
    private Long userId;
    private String name;
    private String username;
    private String filename;
    private Integer produceCount;
    /*private Integer produceCount;
    private Integer friendsCount;
    private Integer subscriptions;
    private Integer reviews;*/
    private String friendship;
}
