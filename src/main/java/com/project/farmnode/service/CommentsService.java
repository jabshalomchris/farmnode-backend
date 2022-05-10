package com.project.farmnode.service;

import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.CommentMapper;
import com.project.farmnode.model.Comment;
import com.project.farmnode.model.NotificationEmail;
import com.project.farmnode.model.Post;
import com.project.farmnode.model.User;
import com.project.farmnode.repo.CommentRepo;
import com.project.farmnode.repo.PostRepo;
import com.project.farmnode.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import java.util.List;

@Service("commentService")
@AllArgsConstructor
public class CommentsService {
    private static final String POST_URL = "";
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final CommentRepo commentRepo;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto, String username) {
        Post post = postRepo.findById(commentsDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: "+commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, userService.getUser(username));
        commentRepo.save(comment);

        String message = mailContentBuilder.build(post.getUser().getName() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getName() + " Commented on your post", user.getUsername(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: "+postId.toString()));
        return commentRepo.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepo.findByUsername(userName);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        //             .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepo.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
