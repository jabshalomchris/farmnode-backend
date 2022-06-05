package com.project.farmnode.service;

import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.dto.ProduceCommentsDto;
import com.project.farmnode.mapper.CommentMapper;
import com.project.farmnode.model.*;
import com.project.farmnode.repository.CommentRepo;
import com.project.farmnode.repository.PostRepo;
import com.project.farmnode.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class CommentsServiceTest {
    @InjectMocks
    private CommentsService serviceUnderTest;
    @Mock
    private PostRepo postRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private UserService userService;
    @Mock
    private CommentRepo commentRepo;
    @Mock
    private MailContentBuilder mailContentBuilder;
    @Mock
    private MailService mailService;

    @Test
    void testAddingComment() {
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Post post = new Post(1L,"testPost","","",1,user,Instant.now(),true);
        CommentsDto commentsDto = new CommentsDto(1L,1L,Instant.now(),"test","user");
        Comment comment = new Comment(1L,"test",post,Instant.now(),user,true);
        //when
        when(postRepo.findById(any())).thenReturn(Optional.of(post));
        when(userService.getUser(any())).thenReturn(user);
        when(commentMapper.map(any(),any(),any())).thenReturn(comment);

        serviceUnderTest.save(commentsDto,"test");

        //then
        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepo).save(commentArgumentCaptor.capture());
        Comment capturedComment = commentArgumentCaptor.getValue();
        assertThat(capturedComment).isEqualTo(comment);
    }

    @Test
    void testGettingAllCommentsForPost() {
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Post post = new Post(1L,"testPost","","",1,user,Instant.now(),true);
        CommentsDto commentsDto = new CommentsDto(1L,1L,Instant.now(),"test","user");

        List<CommentsDto> expectedResult = new ArrayList<>();
        expectedResult.add(commentsDto);expectedResult.add(commentsDto);
        //when
        when(postRepo.findById(any())).thenReturn(Optional.of(post));
        when(commentRepo.findByPost(any()))
                .thenReturn(Stream.of(new Comment(),new Comment()).collect(Collectors.toList()));
        when(commentMapper.mapToDto(any(Comment.class))).thenReturn(commentsDto);

        List<CommentsDto> result=serviceUnderTest.getAllCommentsForPost(1L);

        //then
        assertNotNull(result);
        assertEquals(expectedResult,result);

    }
    @Test
    void testExceptionWhenGettingAllCommentsOfValidUser() {
        //given
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Post post = new Post(1L,"testPost","","",1,user,Instant.now(),true);
        CommentsDto commentsDto = new CommentsDto(1L,1L,Instant.now(),"test","user");ProduceCommentsDto commentsDto1 = new ProduceCommentsDto(1L,1L,"sample","user","12/12/2022","file");
        List<CommentsDto> expectedResult = new ArrayList<>();
        expectedResult.add(commentsDto);expectedResult.add(commentsDto);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(commentRepo.findAllByUser(any()))
                .thenReturn(Stream.of(new Comment(),new Comment()).collect(Collectors.toList()));
        when(commentMapper.mapToDto(any(Comment.class))).thenReturn(commentsDto);

        List<CommentsDto> result=serviceUnderTest.getAllCommentsForUser("testUser");

        //then
        assertNotNull(result);
        assertEquals(expectedResult,result);
    }

    @Test
    void testExceptionWhenGettingAllCommentsOfInvalidUser() {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.getAllCommentsForUser("testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }
}