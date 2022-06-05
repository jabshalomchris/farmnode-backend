package com.project.farmnode.service;

import com.project.farmnode.dto.UserDto;
import com.project.farmnode.enums.FriendshipStatus;
import com.project.farmnode.mapper.UserMapper;
import com.project.farmnode.model.Friend;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.FriendRepo;
import com.project.farmnode.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class FriendServiceTest {
    @InjectMocks
    private FriendService serviceUnderTest;
    @Mock
    private UserRepo userRepo;
    @Mock
    private FriendRepo friendRepo;
    @Mock
    private UserMapper userMapper;

    @Test
    void testSavingNewFriend() {
        //given
        UserDto userDto1 = new UserDto(1L,"test","test","testFile");
        UserDto userDto2 = new UserDto(2L,"test2","test2","testFile");

        User sender = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        User reciever = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);

        //when
        when(userRepo.getById(any())).thenReturn(reciever);
        when(userMapper.mapToDto(any())).thenReturn(userDto2);
        when(userRepo.findByUsername(userDto1.getUsername())).thenReturn(sender);
        when(userRepo.findByUsername(userDto2.getUsername())).thenReturn(reciever);
        when(friendRepo.existsBySenderAndReceiver(any(),any())).thenReturn(false);
        serviceUnderTest.saveFriend(userDto1,1L);

        //then
        verify(friendRepo, times(1)).save(any());

    }

    @Test
    void testSavingAlreadyExistingFriend() {
        //given
        UserDto userDto1 = new UserDto(1L,"test","test","testFile");
        UserDto userDto2 = new UserDto(2L,"test2","test2","testFile");

        User sender = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        User reciever = new User(1L,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);

        //when
        when(userRepo.getById(any())).thenReturn(reciever);
        when(userMapper.mapToDto(any())).thenReturn(userDto2);
        when(userRepo.findByUsername(userDto1.getUsername())).thenReturn(sender);
        when(userRepo.findByUsername(userDto2.getUsername())).thenReturn(reciever);
        when(friendRepo.existsBySenderAndReceiver(any(),any())).thenReturn(true);
        serviceUnderTest.saveFriend(userDto1,1L);

        //then
        verify(friendRepo, times(0)).save(any());

    }

    @Test
    void testGettingFriendsOfUser() throws ParseException {
        //given
        User user = new User(1L,"Test","testing@test4434.com","123","false","123", Instant.now(),true);
        User user1 = new User(2L,"Test2","testing@test4434.com","123","false","123", Instant.now(),true);
        User user2 = new User(3L,"Test3","testing@test4434.com","123","false","123", Instant.now(),true);
        UserDto dto1 = new UserDto(2L,"Test2","testing2","file1") ;
        UserDto dto2 = new UserDto(3L,"Test3","testing2","file2") ;
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse("1/05/2022");
        Friend friend1 = new Friend(1, date1 ,user,user1,FriendshipStatus.APPROVED.toString());
        Friend friend2 = new Friend(1, date1 ,user,user2,FriendshipStatus.APPROVED.toString());
        List<Friend> friendList1 = new ArrayList<>();
        friendList1.add(friend1);
        List<Friend> friendList2 = new ArrayList<>();
        friendList2.add(friend2);
        List<UserDto> expectedResult = new ArrayList<>();
        expectedResult.add(dto1);
        expectedResult.add(dto1);

        Friend friend = new Friend();

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(friendRepo.findBySenderAndApprovalStatus(any(),any())).thenReturn(friendList1);
        when(friendRepo.findByReceiverAndApprovalStatus(any(),any())).thenReturn(friendList2);
        when(userRepo.getById(any())).thenReturn(user1);
        when(userMapper.mapToDto(user1)).thenReturn(dto1);

        List<UserDto> result = serviceUnderTest.getFriendsOfUser("test");

        //then
        assertNotNull(result);
        assertEquals(expectedResult,result);

    }

    @Test
    void testExceptionThrownWhenGettingFriendsOfUser()  {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.getFriendsOfUser("testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");

    }

    @Test
    void testGettingOutBoundRequests() throws ParseException {
        //given
        User user = new User(1L,"Test","testing@test4434.com","123","false","123", Instant.now(),true);
        User user1 = new User(2L,"Test2","testing@test4434.com","123","false","123", Instant.now(),true);
        User user2 = new User(3L,"Test3","testing@test4434.com","123","false","123", Instant.now(),true);
        UserDto dto1 = new UserDto(2L,"Test2","testing2","file1") ;
        UserDto dto2 = new UserDto(3L,"Test3","testing3","file1") ;

        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse("1/05/2022");
        Friend friend1 = new Friend(1, date1 ,user,user1,FriendshipStatus.PENDING.toString());
        Friend friend2 = new Friend(1, date1 ,user,user2,FriendshipStatus.PENDING.toString());
        List<Friend> friendList = new ArrayList<>();
        friendList.add(friend1);
        friendList.add(friend2);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(friendRepo.findBySenderAndApprovalStatus(any(),any())).thenReturn(friendList);
        when(userRepo.getById(2L)).thenReturn(user1);
        when(userRepo.getById(3L)).thenReturn(user2);
        when(userMapper.mapToDto(user1)).thenReturn(dto1);
        when(userMapper.mapToDto(user2)).thenReturn(dto2);

        List<UserDto> result = serviceUnderTest.getOutBoundRequests("test");

        //then
        assertNotNull(result);
        assertTrue(result.size()==2);
    }

    @Test
    void testExceptionThrownWhenGettingOutBoundRequests()  {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.getOutBoundRequests("testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");

    }

    @Test
    void testGettingInboundRequests() throws ParseException {
        //given
        User user = new User(1L,"Test","testing@test4434.com","123","false","123", Instant.now(),true);
        User user1 = new User(2L,"Test2","testing@test4434.com","123","false","123", Instant.now(),true);
        User user2 = new User(3L,"Test3","testing@test4434.com","123","false","123", Instant.now(),true);
        UserDto dto1 = new UserDto(2L,"Test2","testing2","file1") ;
        UserDto dto2 = new UserDto(3L,"Test3","testing3","file1") ;

        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse("1/05/2022");
        Friend friend1 = new Friend(1, date1 ,user1,user,FriendshipStatus.PENDING.toString());
        Friend friend2 = new Friend(1, date1 ,user2,user,FriendshipStatus.PENDING.toString());
        List<Friend> friendList = new ArrayList<>();
        friendList.add(friend1);
        friendList.add(friend2);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(friendRepo.findByReceiverAndApprovalStatus(any(),any())).thenReturn(friendList);
        when(userRepo.getById(2L)).thenReturn(user1);
        when(userRepo.getById(3L)).thenReturn(user2);
        when(userMapper.mapToDto(user1)).thenReturn(dto1);
        when(userMapper.mapToDto(user2)).thenReturn(dto2);

        List<UserDto> result = serviceUnderTest.getInboundRequests("test");

        //then
        assertNotNull(result);
        assertTrue(result.size()==2);
    }

    @Test
    void testExceptionThrownWhenGettingInBoundRequests()  {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->serviceUnderTest.getInboundRequests("testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");

    }

    @Test
    void testChangingFriendshipStatus() {

        //when
        doNothing().when(friendRepo).updateStatusByReceiver(any(),anyLong(),anyLong());

        serviceUnderTest.changeFriendStatus("APPROVED", 1,1);

        //then
        verify(friendRepo, times(1)).updateStatusByReceiver(any(),anyLong(),anyLong());
    }

    @Test
    @Disabled
    void deleteRequest() {
    }
}