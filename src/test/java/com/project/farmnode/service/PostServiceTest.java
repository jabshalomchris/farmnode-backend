package com.project.farmnode.service;

import com.project.farmnode.dto.PostRequest;
import com.project.farmnode.dto.PostResponse;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.mapper.PostMapper;
import com.project.farmnode.model.Post;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.PostRepo;
import com.project.farmnode.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class PostServiceTest {

    @InjectMocks
    private PostService serviceUnderTest;
    @Mock
    private PostRepo postRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private  UserService userService;
    @Mock
    private PostMapper postMapper;

    @Test
    void testPostSaving() {

        //given
        User user = new User(null,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        PostRequest postRequest = new PostRequest(1L,"testing Post","test","","user");
        Post post = new Post(1L,"testing Post","","test",0,user,Instant.now(),true);

        //when
        when(userService.getUser(user.getUsername())).thenReturn(user);
        when(postMapper.map(any(),any())).thenReturn(post);
        serviceUnderTest.save(postRequest, user.getUsername());

        //then
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepo).save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();
        assertThat(capturedPost).isEqualTo(post);
    }

    @Test
    void testGettingPostById() {
        Post post = new Post(1L,"testing Post","","test",0,null,Instant.now(),true);

        PostResponse postResponse = new PostResponse(1L,"test post","","","user",0,"ago");

        //when
        when(postRepo.findById(any())).thenReturn(java.util.Optional.of(post));
        when(postMapper.mapToDto(any(Post.class))).thenReturn(postResponse);

        //then
        PostResponse result = serviceUnderTest.getPost(1L);

        assertEquals(postResponse,result ); // check dto result
        Mockito.verify(postMapper).mapToDto(post); //verify the mapper
    }

    @Test
    void testGettingAllPost() {
        Post post = new Post(1L,"testing Post","","test",0,null,Instant.now(),true);
        PostResponse postResponse = new PostResponse(1L,"test post","","","user",0,"ago");

        List<Post> postResult = new ArrayList<>();
        postResult.add(post);

        List<PostResponse> dtoResult = new ArrayList<>();
        dtoResult.add(postResponse);

        //when
        when(postRepo.findAll()).thenReturn(postResult);
        when(postMapper.mapToDto(any(Post.class))).thenReturn(postResponse);

        //then
        List<PostResponse> resultList = serviceUnderTest.getAllPosts();

        assertEquals(1, resultList.size()); // check size
        assertEquals(dtoResult, resultList); // check size
        assertEquals(postResponse, resultList.get(0)); // check dto result
        Mockito.verify(postMapper).mapToDto(post); //verify the mapper
    }

    @Test
    void testExceptionIsThrownWhenGettingProduceByInvalidUser() {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.getPostsByUsername("testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void testGetPostByUsername() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        Post post = new Post(1L,"testing Post","","test",0,null,Instant.now(),true);
        PostResponse postResponse = new PostResponse(1L,"test post","","","user",0,"ago");

        List<Post> postResult = new ArrayList<>();
        postResult.add(post);

        List<PostResponse> dtoResult = new ArrayList<>();
        dtoResult.add(postResponse);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(postRepo.findByUser(any())).thenReturn(postResult);
        when(postMapper.mapToDto(any(Post.class))).thenReturn(postResponse);

        //then
        List<PostResponse> resultList = serviceUnderTest.getPostsByUsername(user.getUsername());

        assertEquals(1, resultList.size()); // check size
        assertEquals(dtoResult, resultList); // check size
        assertEquals(postResponse, resultList.get(0)); // check dto result
        Mockito.verify(postMapper).mapToDto(post); //verify the mapper
    }

    @Test
    void testExceptionIsThrownWhenGettingProduceByInvalidUserAndStatus() {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.getPostsByUsernameAndStatus("testing",true))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void testGetPostByUsernameAndStatus() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        Post post = new Post(1L,"testing Post","","test",0,null,Instant.now(),true);
        PostResponse postResponse = new PostResponse(1L,"test post","","","user",0,"ago");

        List<Post> postResult = new ArrayList<>();
        postResult.add(post);

        List<PostResponse> dtoResult = new ArrayList<>();
        dtoResult.add(postResponse);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(postRepo.findByUserAndStatus(any(),anyBoolean())).thenReturn(postResult);
        when(postMapper.mapToDto(any(Post.class))).thenReturn(postResponse);

        //then
        List<PostResponse> resultList = serviceUnderTest.getPostsByUsernameAndStatus(user.getUsername(),true);

        assertEquals(1, resultList.size()); // check size
        assertEquals(dtoResult, resultList); // check size
        assertEquals(postResponse, resultList.get(0)); // check dto result
        Mockito.verify(postMapper).mapToDto(post); //verify the mapper
    }
}