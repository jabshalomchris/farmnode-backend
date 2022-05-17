package com.project.farmnode.dto.ProduceRequest;

import javax.validation.constraints.NotNull;

public class RequestDto {
    private long requestId;
    private @NotNull long userId;
}
