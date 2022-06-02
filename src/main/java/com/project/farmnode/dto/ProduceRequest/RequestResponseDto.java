package com.project.farmnode.dto.ProduceRequest;

import com.project.farmnode.model.RequestItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponseDto {
    private long requestId;
    private long growerId;
    private String growerName;
    private long buyerId;
    private String buyerName;
    private String requestStatus;
    private String message;
    private List<RequestItemResponseDto> requestItem;
    private String createdDate;
}
