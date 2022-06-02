package com.project.farmnode.dto.ProduceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDbDto {

    private String request_id;
    private String request_status;
    private String created_date;
    private long buyer_id;
    private long grower_id;
    private double price;
    private int quantity;
    private long produce_id;

}
