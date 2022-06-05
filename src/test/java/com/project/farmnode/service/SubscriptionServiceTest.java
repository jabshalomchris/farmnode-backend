package com.project.farmnode.service;

import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.mapper.ProduceMapper;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.Subscription;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.SubscriptionRepo;
import com.project.farmnode.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class SubscriptionServiceTest {
    @InjectMocks
    private SubscriptionService serviceUnderTest;
    @Mock
    private SubscriptionRepo subscriptionRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ProduceMapper produceMapper;

    @Test
    void testAddSubscription() {
        //given
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        Subscription subscription = new Subscription(1L,user,produce);
        //when
        serviceUnderTest.subscribe(user,produce);
        //then
        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepo).save(subscriptionArgumentCaptor.capture());

        Subscription capturedSubscription = subscriptionArgumentCaptor.getValue();
        assertThat(capturedSubscription.getUser()).isEqualTo(subscription.getUser());
        assertThat(capturedSubscription.getProduce()).isEqualTo(subscription.getProduce());
    }

    @Test
    void testSuccessfulUnsubscribe() {
        //given
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        //when
        when(subscriptionRepo.deleteByUserAndProduce(any(),any())).thenReturn(1L);
        serviceUnderTest.unsubscribe(user,produce);

        //then
        verify(subscriptionRepo, times(1)).deleteByUserAndProduce(any(),any());
    }

    @Test
    void testValidSubscriptionCheck() {
        //given
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        //when
        when(subscriptionRepo.existsByUserAndProduce(any(),any())).thenReturn(true);
        boolean result = serviceUnderTest.checkSubscription(user,produce);

        //then
        assertEquals(result,true);
    }

    @Test
    void testInvalidSubscriptionCheck() {
        //given
        User user = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        //when
        when(subscriptionRepo.existsByUserAndProduce(any(),any())).thenReturn(false);
        boolean result = serviceUnderTest.checkSubscription(user,produce);

        //then
        assertNotEquals(result,true);
    }



    @Test
    void testExceptionWhenFindingByInvalidUser() {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.findByUser("testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void testFindingByUser() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        Subscription subscription = new Subscription(1L,user,produce1);
        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname","testingUname","file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(subscription);

        List<ProduceDto> dtoList = new ArrayList<>();
        dtoList.add(dto1);

        Map<String, List<ProduceDto>> map = new HashMap<>();
        map.put("testingUname",dtoList);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(subscriptionRepo.findByUser(any())).thenReturn(subscriptionList);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);


        Map<String, List<ProduceDto>> result = serviceUnderTest.findByUser("test");
        //then
        assertEquals(map,result);
 }

    @Test
    void testFindByUserDetailed() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        Subscription subscription = new Subscription(1L,user,produce1);
        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname","testingUname","file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(subscription);

        List<ProduceDto> dtoList = new ArrayList<>();
        dtoList.add(dto1);


        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(subscriptionRepo.findByUser(any())).thenReturn(subscriptionList);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);


        List<ProduceDto> result= serviceUnderTest.findByUserDetailed("test");
        //then
        assertEquals(dtoList,result);
    }
    @Test
    void testWhetherExceptionThrownFindByUserDetailed() {
        //when
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.findByUserDetailed("testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");

    }
}