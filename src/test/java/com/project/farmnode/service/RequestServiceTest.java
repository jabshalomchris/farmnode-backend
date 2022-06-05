package com.project.farmnode.service;

import com.project.farmnode.dto.ProduceRequest.RequestDto;
import com.project.farmnode.dto.ProduceRequest.RequestItemsDto;
import com.project.farmnode.dto.ProduceRequest.RequestResponseDto;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.Request;
import com.project.farmnode.model.RequestItem;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import com.project.farmnode.repository.RequestItemsRepo;
import com.project.farmnode.repository.RequestRepo;
import com.project.farmnode.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class RequestServiceTest {
    @InjectMocks
    private RequestService serviceUnderTest;
    @Mock
    private UserRepo userRepo;
    @Mock
    private RequestRepo requestRepo;
    @Mock
    private ProduceRepo produceRepo;
    @Mock
    private RequestItemsRepo requestItemsRepo;
    @Mock
    private UserService userService;

    @Test
    void testingSavingRequest() {
        //given
        User buyer = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        User grower = new User(2L,"Testing2","testingUname2","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",grower);
        RequestItemsDto item1 = new RequestItemsDto(produce.getProduceId(),produce.getProduceName(),produce.getPrice(),1,10);
        List<RequestItemsDto> itemsDtoList = new ArrayList<>();
        itemsDtoList.add(item1);
        RequestDto requestDto = new RequestDto();
        requestDto.setBuyerId(buyer.getUserId());
        requestDto.setGrowerId(grower.getUserId());
        requestDto.setMessage("");
        requestDto.setRequestItem(itemsDtoList);
        //when
        when(userService.getUser(any())).thenReturn(buyer);
        when(userRepo.findById(any())).thenReturn(java.util.Optional.of(grower));
        when(produceRepo.findById(any())).thenReturn(java.util.Optional.of(produce));
        serviceUnderTest.save(requestDto, "test");
        //then
        verify(requestRepo, times(1)).save(any());
        verify(requestItemsRepo, times(1)).save(any());

    }
    @Test
    void testingExceptionWhileSavingRequest() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        RequestDto requestDto = new RequestDto();
        requestDto.setGrowerId(1L);
        //when
        when(userService.getUser(any())).thenReturn(user);
        when(userRepo.findById(any())).thenReturn(null);

        //serviceUnderTest.save(requestDto,"");

        assertThatThrownBy(()->serviceUnderTest.save(requestDto,any()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testGettingRequestsByBuyer() {
        User buyer = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        User grower = new User(2L,"Testing2","testingUname2","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",grower);
        RequestItem requestItem = new RequestItem(1L,1,10,null,produce);
        List<RequestItem> requestItemList = new ArrayList<>();
        requestItemList.add(requestItem);
        Request request = new Request(1L,"PENDING","",requestItemList,buyer,grower,Instant.now());
        List<Request> requestList = new ArrayList<>();
        requestList.add(request);
        //when
        when(userRepo.findByUsername(any())).thenReturn(buyer);
        when(requestRepo.findAllByBuyerIdOrderByCreatedDateDesc(any())).thenReturn(requestList);

        List<RequestResponseDto> result = serviceUnderTest.getRequestsByBuyer("test");

        //then
        assertNotNull(result);
        assertThat(result.size()==1);
    }

    @Test
    void testGettingRequestsByBuyerWithNullRequestStatus() {
        User buyer = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        User grower = new User(2L,"Testing2","testingUname2","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",grower);
        RequestItem requestItem = new RequestItem(1L,1,10,null,produce);
        List<RequestItem> requestItemList = new ArrayList<>();
        requestItemList.add(requestItem);
        Request request = new Request(1L,null,"",requestItemList,buyer,grower,Instant.now());
        List<Request> requestList = new ArrayList<>();
        requestList.add(request);
        //when
        when(userRepo.findByUsername(any())).thenReturn(buyer);
        when(requestRepo.findAllByBuyerIdOrderByCreatedDateDesc(any())).thenReturn(requestList);

        List<RequestResponseDto> result = serviceUnderTest.getRequestsByBuyer("test");

        //then
        assertNotNull(result);
        assertThat(result.size()==1);
    }




    @Test
    void testExceptionWhenGettingRequestsByBuyer() {
        //when
        when(userRepo.findByUsername(any())).thenReturn(null);

        //then
        assertThatThrownBy(()->serviceUnderTest.getRequestsByBuyer(any()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void testGettingRequestsByGrower() {
        User buyer = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        User grower = new User(2L,"Testing2","testingUname2","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",grower);
        RequestItem requestItem = new RequestItem(1L,1,10,null,produce);
        List<RequestItem> requestItemList = new ArrayList<>();
        requestItemList.add(requestItem);
        Request request = new Request(1L,"PENDING","",requestItemList,buyer,grower,Instant.now());
        List<Request> requestList = new ArrayList<>();
        requestList.add(request);
        //when
        when(userRepo.findByUsername(any())).thenReturn(buyer);
        when(requestRepo.findAllByGrowerIdOrderByCreatedDateDesc(any())).thenReturn(requestList);

        List<RequestResponseDto> result = serviceUnderTest.getRequestsByGrower("test");

        //then
        assertNotNull(result);
        assertThat(result.size()==1);
    }

    @Test
    void testGettingRequestsByGrowerWithNullRequestStatus() {
        User buyer = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        User grower = new User(2L,"Testing2","testingUname2","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",grower);
        RequestItem requestItem = new RequestItem(1L,1,10,null,produce);
        List<RequestItem> requestItemList = new ArrayList<>();
        requestItemList.add(requestItem);
        Request request = new Request(1L,null,"",requestItemList,buyer,grower,Instant.now());
        List<Request> requestList = new ArrayList<>();
        requestList.add(request);
        //when
        when(userRepo.findByUsername(any())).thenReturn(buyer);
        when(requestRepo.findAllByGrowerIdOrderByCreatedDateDesc(any())).thenReturn(requestList);

        List<RequestResponseDto> result = serviceUnderTest.getRequestsByGrower("test");

        //then
        assertNotNull(result);
        assertThat(result.size()==1);
    }

    @Test
    void testExceptionWhileGettingRequestsByGrower() {
        //when
        when(userRepo.findByUsername(any())).thenReturn(null);

        //then
        assertThatThrownBy(()->serviceUnderTest.getRequestsByGrower(any()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void testExceptionWhenGettingRequestById() {
        //when
        when(requestRepo.findById(any())).thenReturn(null);

        //then
        assertThatThrownBy(()->serviceUnderTest.getRequestById(any()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testGettingRequestById() {
        User buyer = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        User grower = new User(2L,"Testing2","testingUname2","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",grower);
        RequestItem requestItem = new RequestItem(1L,1,10,null,produce);
        List<RequestItem> requestItemList = new ArrayList<>();
        requestItemList.add(requestItem);
        Request request = new Request(1L,"PENDING","",requestItemList,buyer,grower,Instant.now());

        //when
        when(requestRepo.findById(any())).thenReturn(java.util.Optional.of(request));

        //then
        RequestResponseDto result = serviceUnderTest.getRequestById(1L);

        assertNotNull(result);
        assertEquals(result.getBuyerId(),buyer.getUserId());
        assertEquals(result.getGrowerId(),grower.getUserId());
    }

    @Test
    void testGettingRequestByIdWithNullStatus() {
        User buyer = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        User grower = new User(2L,"Testing2","testingUname2","123","false","123", Instant.now(),true);
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",grower);
        RequestItem requestItem = new RequestItem(1L,1,10,null,produce);
        List<RequestItem> requestItemList = new ArrayList<>();
        requestItemList.add(requestItem);
        Request request = new Request(1L,null,"",requestItemList,buyer,grower,Instant.now());

        //when
        when(requestRepo.findById(any())).thenReturn(java.util.Optional.of(request));

        //then
        RequestResponseDto result = serviceUnderTest.getRequestById(1L);

        assertNotNull(result);
        assertEquals(result.getBuyerId(),buyer.getUserId());
        assertEquals(result.getGrowerId(),grower.getUserId());
    }

    @Test
    void testApprovedRequestStatusUpdate() {
        //when
        doNothing().when(requestRepo).updateRequestStatus(anyString(),anyInt());
        serviceUnderTest.updateRequestStatus( 1,"APPROVED");

        //then
        verify(requestRepo, times(1)).updateRequestStatus(anyString(),anyInt());
        verify(requestRepo).updateRequestStatus("APPROVED", 1); //verify whether status goes as approved
    }

    @Test
    void testPendingRequestStatusUpdate() {
        //when
        doNothing().when(requestRepo).updateRequestStatus(anyString(),anyInt());
        serviceUnderTest.updateRequestStatus( 1,"");

        //then
        verify(requestRepo, times(1)).updateRequestStatus(anyString(),anyInt());
        verify(requestRepo).updateRequestStatus("PENDING", 1); //verify whether status goes as pending
    }

    @Test
    void testDeclinedRequestStatusUpdate() {
        //when
        doNothing().when(requestRepo).updateRequestStatus(anyString(),anyInt());
        serviceUnderTest.updateRequestStatus( 1,"DECLINED");

        //then
        verify(requestRepo, times(1)).updateRequestStatus(anyString(),anyInt());
        verify(requestRepo).updateRequestStatus("DECLINED", 1); //verify whether status goes as pending
    }



}