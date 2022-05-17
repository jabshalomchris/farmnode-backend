package com.project.farmnode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
    private Long commentId;
    private Long postId;
    private Instant createdDate;
    private String commentText;
    private String userName;
}
