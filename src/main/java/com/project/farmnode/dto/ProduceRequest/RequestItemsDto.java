package com.project.farmnode.dto.ProduceRequest;

import javax.validation.constraints.NotNull;

public class RequestItemsDto {

    private @NotNull double price;
    private @NotNull int quantity;
    private @NotNull long requestId;
    private @NotNull long produceId;
}
