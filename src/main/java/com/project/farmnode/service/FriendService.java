package com.project.farmnode.service;

import com.project.farmnode.model.Friend;
import com.project.farmnode.model.User;
import com.project.farmnode.repo.FriendRepo;
import com.project.farmnode.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FriendService {
    /*private final UserRepo userRepo;
    private final FriendRepo friendRepo;

    public void saveFriend(UserDto userDto1, Long id) throws NullPointerException{
        User user = new User();
        user = userRepo.getOne(id);
        UserDto userDto2 = modelMapper.map(user,UserDto.class);

        Friend friend = new Friend();
        User user1 = userRepo.findUserByEmail(userDto1.getEmail());
        User user2 = userRepo.findUserByEmail(userDto2.getEmail());
        User firstuser = user1;
        User seconduser = user2;
        if(user1.getId() > user2.getId()){
            firstuser = user2;
            seconduser = user1;
        }
        if( !(friendRepo.existsByFirstUserAndSecondUser(firstuser,seconduser)) ){
            friend.setCreatedDate(new Date());
            friend.setFirstUser(firstuser);
            friend.setSecondUser(seconduser);
            friendRepo.save(friend);
        }
    }*/
}
