package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.config.SecurityConfig;
import com.project.farmnode.dto.PostRequest;
import com.project.farmnode.dto.PostResponse;
import com.project.farmnode.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostRequest postRequest, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        postService.save(postRequest,username);
        return new ResponseEntity<>(new ApiResponse(true, "Posted"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }

    @GetMapping("by-user-status/{username}/{status}")
    public ResponseEntity<List<PostResponse>> getPostsByUsernameAndStatus(@PathVariable("username") String username,
                                                                          @PathVariable("status") boolean status) {
        return status(HttpStatus.OK).body(postService.getPostsByUsernameAndStatus(username,status));
    }

    //getting the logged-in user
    @GetMapping("/gettinguserdetails")
    @ResponseBody
    public String currentUserNameSimple(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return principal.getName();
    }

}
