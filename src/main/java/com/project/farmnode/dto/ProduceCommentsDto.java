package com.project.farmnode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduceCommentsDto {
    private Long produceCommentId;
    private Long produceId;
    private Instant createdDate;
    private String text;
    private String userName;
}
