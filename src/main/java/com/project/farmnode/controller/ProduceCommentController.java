package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.dto.ProduceCommentsDto;
import com.project.farmnode.service.CommentsService;
import com.project.farmnode.service.ProduceCommentService;
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
@RequestMapping("/api/produce/comments/")
@AllArgsConstructor
public class ProduceCommentController {
    @Resource(name = "ProduceCommentService")
    private final ProduceCommentService produceCommentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProduceComment(@RequestBody ProduceCommentsDto produceCommentsDto, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        produceCommentService.save(produceCommentsDto,username);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Commented"), HttpStatus.CREATED);
    }

    @GetMapping("/by-produce/{produceId}")
    public ResponseEntity<List<ProduceCommentsDto>> getAllCommentsForProduce(@PathVariable Long produceId) {
        return ResponseEntity.status(OK).body(produceCommentService.getAllCommentsForProduce(produceId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<ProduceCommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(produceCommentService.getAllCommentsForUser(userName));
    }

}
