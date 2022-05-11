package com.project.farmnode.service;

import com.project.farmnode.dto.UserDto;
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

    public void saveFriend(UserDto userDto1, Long id) throws NullPointerException{
        User user = userRepo.getOne(id);
       // user = userRepo.getOne(id);
        UserDto userDto2 = userMapper.mapToDto(user);

        Friend friend = new Friend();
        User user1 = userRepo.findByUsername(userDto1.getUsername());   // change to email when needed
        User user2 = userRepo.findByUsername(userDto2.getUsername());   // change to email when needed
        User firstuser = user1;
        User seconduser = user2;
        if(user1.getUserId() > user2.getUserId()){
            firstuser = user2;
            seconduser = user1;
        }
        if( !(friendRepo.existsByFirstUserAndSecondUser(firstuser,seconduser)) ){
            friend.setCreatedDate(new Date());
            friend.setFirstUser(firstuser);
            friend.setSecondUser(seconduser);
            friendRepo.save(friend);
        }
    }

    public List<UserDto> getFriends(String userName){
        User currentUser = userRepo.findByUsername(userName);
        if(currentUser==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        List<Friend> friendsByFirstUser = friendRepo.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepo.findBySecondUser(currentUser);
        List<UserDto> friendUsers = new ArrayList<>();
        UserDto userDto;

        /*
            suppose there are 3 users with id 1,2,3.
            if user1 add user2 as friend database record will be first user = user1 second user = user2
            if user3 add user2 as friend database record will be first user = user2 second user = user3
            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user
         */
        for (Friend friend : friendsByFirstUser) {

             userDto = userMapper.mapToDto(userRepo.getById(friend.getSecondUser().getUserId()));
            friendUsers.add(userDto);
        }
        for (Friend friend : friendsBySecondUser) {
             userDto = userMapper.mapToDto(userRepo.getById(friend.getSecondUser().getUserId()));
            friendUsers.add(userDto);
        }
        return friendUsers;

    }
}
