package com.project.farmnode.repository;

import com.project.farmnode.model.Post;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserAndStatus(User user, boolean status);
}
