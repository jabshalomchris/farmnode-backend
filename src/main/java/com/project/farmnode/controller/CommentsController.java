package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.service.CommentsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
    @Resource(name = "commentService")
    private final CommentsService commentsService;

    @PostMapping
    public ResponseEntity<ApiResponse> createComment(@RequestBody CommentsDto commentsDto, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        commentsService.save(commentsDto,username);
        return new ResponseEntity<>(new ApiResponse(true, "Commented"), HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK).body(commentsService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentsService.getAllCommentsForUser(userName));
    }

}
