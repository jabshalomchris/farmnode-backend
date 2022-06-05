package com.project.farmnode.service;

import com.project.farmnode.dto.UserDto;
import com.project.farmnode.enums.FriendshipStatus;
import com.project.farmnode.mapper.UserMapper;
import com.project.farmnode.model.Friend;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.FriendRepo;
import com.project.farmnode.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FriendService {
    private final UserRepo userRepo;
    private final FriendRepo friendRepo;
    private final UserMapper userMapper;

    /*public void saveFriend(UserDto userDto1, Long id) throws NullPointerException{
        User user = userRepo.getOne(id);
       // user = userRepo.getOne(id);
        UserDto userDto2 = userMapper.mapToDto(user);

        Friend friend = new Friend();
        User user1 = userRepo.findByUsername(userDto1.getUsername());   // change to email when needed
        User user2 = userRepo.findByUsername(userDto2.getUsername());   // change to email when needed
        User firstUser = user1;
        User secondUser = user2;
        if(user1.getUserId() > user2.getUserId()){
            firstUser = user2;
            secondUser = user1;
        }
        if( !(friendRepo.existsByFirstUserAndSecondUser(firstUser,secondUser)) ){
            friend.setCreatedDate(new Date());
            friend.setFirstUser(firstUser);
            friend.setSecondUser(secondUser);
            friend.setApprovalStatus(FriendshipStatus.PENDING.toString());
            friendRepo.save(friend);
        }
    }*/

    public void saveFriend(UserDto userDto1, Long id) throws NullPointerException{
        User user = userRepo.getById(id);
        // user = userRepo.getOne(id);
        UserDto userDto2 = userMapper.mapToDto(user);

        Friend friend = new Friend();
        User sender = userRepo.findByUsername(userDto1.getUsername());   // change to email when needed
        User receiver = userRepo.findByUsername(userDto2.getUsername());   // change to email when needed


        if(friendRepo.existsBySenderAndReceiver(sender,receiver) == false && friendRepo.existsBySenderAndReceiver(receiver,sender)==false){
            friend.setCreatedDate(new Date());
            friend.setSender(sender);
            friend.setReceiver(receiver);
            friend.setApprovalStatus(FriendshipStatus.PENDING.toString());
            friendRepo.save(friend);
        }

    }


    public List<UserDto> getFriendsOfUser(String userName){
        User currentUser = userRepo.findByUsername(userName);
        if(currentUser==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        List<Friend> friendsBySender = friendRepo.findBySenderAndApprovalStatus(currentUser, FriendshipStatus.APPROVED.toString());
        List<Friend> friendsByReceiver = friendRepo.findByReceiverAndApprovalStatus(currentUser, FriendshipStatus.APPROVED.toString());
        List<UserDto> friendUsers = new ArrayList<>();
        UserDto userDto;

        for (Friend friend : friendsBySender) {

             userDto = userMapper.mapToDto(userRepo.getById(friend.getReceiver().getUserId()));
            friendUsers.add(userDto);
        }
        for (Friend friend : friendsByReceiver) {
             userDto = userMapper.mapToDto(userRepo.getById(friend.getSender().getUserId()));
            friendUsers.add(userDto);
        }
        return friendUsers;

    }

    public List<UserDto> getOutBoundRequests(String userName){
        User currentUser = userRepo.findByUsername(userName);
        if(currentUser==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        List<Friend> friendsBySender = friendRepo.findBySenderAndApprovalStatus(currentUser, FriendshipStatus.PENDING.toString());
        List<UserDto> receivers = new ArrayList<>();
        UserDto userDto;

        for (Friend friend : friendsBySender) {
            userDto = userMapper.mapToDto(userRepo.getById(friend.getReceiver().getUserId()));
            receivers.add(userDto);
        }
        return receivers;
    }

    public List<UserDto> getInboundRequests(String userName){
        User currentUser = userRepo.findByUsername(userName);
        if(currentUser==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        List<Friend> friendsByReceiver = friendRepo.findByReceiverAndApprovalStatus(currentUser, FriendshipStatus.PENDING.toString());
        List<UserDto> senders = new ArrayList<>();
        UserDto userDto;

        for (Friend friend : friendsByReceiver) {
            userDto = userMapper.mapToDto(userRepo.getById(friend.getSender().getUserId()));
            senders.add(userDto);
        }
        return senders;
    }

    public void changeFriendStatus(String status, long senderId, long receiverId){
        friendRepo.updateStatusByReceiver(status,senderId, receiverId);
    }

    public void deleteRequest(User sender, User receiver){
        friendRepo.deleteBySenderAndReceiver(sender,receiver);
    }


    /*public List<UserDto> getFriends(String userName){
        User currentUser = userRepo.findByUsername(userName);
        if(currentUser==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        List<Friend> friendsByFirstUser = friendRepo.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepo.findBySecondUser(currentUser);
        List<UserDto> friendUsers = new ArrayList<>();
        UserDto userDto;

        *//*
            suppose there are 3 users with id 1,2,3.
            if user1 add user2 as friend database record will be first user = user1 second user = user2
            if user3 add user2 as friend database record will be first user = user2 second user = user3
            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user
         *//*
        for (Friend friends : friendsByFirstUser) {

             userDto = userMapper.mapToDto(userRepo.getById(friend.getSecondUser().getUserId()));
            friendUsers.add(userDto);
        }
        for (Friend friend : friendsBySecondUser) {
             userDto = userMapper.mapToDto(userRepo.getById(friend.getSecondUser().getUserId()));
            friendUsers.add(userDto);
        }
        return friendUsers;

    }*/
}
