package com.project.farmnode.repository;

import com.project.farmnode.model.Comment;
import com.project.farmnode.model.Post;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);
}
