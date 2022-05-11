package com.project.farmnode.service;

import com.project.farmnode.dto.PostRequest;
import com.project.farmnode.dto.PostResponse;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.PostMapper;
import com.project.farmnode.model.Post;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.PostRepo;
import com.project.farmnode.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest, String Username) {
        postRepo.save(postMapper.map(postRequest, userService.getUser(Username)));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: "+id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepo.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        //                    .orElseThrow(() -> new ResourceNotFoundException("User not found with the username: " +username));
        return postRepo.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }


}
