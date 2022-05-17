package com.project.farmnode.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.project.farmnode.dto.PostRequest;
import com.project.farmnode.dto.PostResponse;
import com.project.farmnode.model.Post;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.CommentRepo;
import com.project.farmnode.repository.PostRepo;
import com.project.farmnode.repository.ProduceRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepo commentRepo;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, User user);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    /*@Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")*/
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepo.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }


}
