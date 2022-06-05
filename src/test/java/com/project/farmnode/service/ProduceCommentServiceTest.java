package com.project.farmnode.service;

import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.dto.ProduceCommentsDto;
import com.project.farmnode.mapper.ProduceCommentMapper;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.ProduceComment;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceCommentRepo;
import com.project.farmnode.repository.ProduceRepo;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class ProduceCommentServiceTest {
    @InjectMocks
    private ProduceCommentService serviceUnderTest;
    @Mock
    private ProduceRepo produceRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserService userService;
    @Mock
    private ProduceCommentMapper produceCommentMapper;
    @Mock
    private ProduceCommentRepo produceCommentRepo;
    @Mock
    private MailContentBuilder mailContentBuilder;
    @Mock
    private MailService mailService;

    @Test
    void testAddingProduceComment() {
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        ProduceCommentsDto produceCommentsDto = new ProduceCommentsDto(1L,1L,"sample","test","now","file");
        ProduceComment produceComment = new ProduceComment(1L,"sample",produce,Instant.now(),user);
        //when
        when(produceRepo.findById(any())).thenReturn(Optional.of(produce));
        when(userService.getUser(any())).thenReturn(user);
        when(produceCommentMapper.map(any(),any(),any())).thenReturn(produceComment);
        serviceUnderTest.save(produceCommentsDto,"test");

        //then
        ArgumentCaptor<ProduceComment> produceCommentArgumentCaptor = ArgumentCaptor.forClass(ProduceComment.class);
        verify(produceCommentRepo).save(produceCommentArgumentCaptor.capture());
        ProduceComment capturedProduceComment = produceCommentArgumentCaptor.getValue();
        assertThat(capturedProduceComment).isEqualTo(produceComment);
    }

    @Test
    void testGettingAllCommentsForProduce() {
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        ProduceCommentsDto commentsDto1 = new ProduceCommentsDto(1L,1L,"sample","user","12/12/2022","file");
        List<ProduceCommentsDto> expectedResult = new ArrayList<>();
        expectedResult.add(commentsDto1);expectedResult.add(commentsDto1);
        //when
        when(produceRepo.findById(any())).thenReturn(Optional.of(produce));
        when(produceCommentRepo.findByProduceOrderByCreatedDateDesc(any()))
                .thenReturn(Stream.of(new ProduceComment(),new ProduceComment()).collect(Collectors.toList()));
        when(produceCommentMapper.mapToDto(any(ProduceComment.class))).thenReturn(commentsDto1);

        List<ProduceCommentsDto> result=serviceUnderTest.getAllCommentsForProduce(1L);

        //then
        assertNotNull(result);
        assertEquals(expectedResult,result);

    }
    @Test
    void testExceptionWhenGettingAllCommentsOfValidUser() {
        //given
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        ProduceCommentsDto commentsDto1 = new ProduceCommentsDto(1L,1L,"sample","user","12/12/2022","file");
        List<ProduceCommentsDto> expectedResult = new ArrayList<>();
        expectedResult.add(commentsDto1);expectedResult.add(commentsDto1);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(produceCommentRepo.findAllByUser(any()))
                .thenReturn(Stream.of(new ProduceComment(),new ProduceComment()).collect(Collectors.toList()));
        when(produceCommentMapper.mapToDto(any(ProduceComment.class))).thenReturn(commentsDto1);

        List<ProduceCommentsDto> result=serviceUnderTest.getAllCommentsForUser("testUser");

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