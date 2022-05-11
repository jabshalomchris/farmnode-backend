package com.project.farmnode.controller;

import com.project.farmnode.dto.UserDto;
import com.project.farmnode.mapper.UserMapper;
import com.project.farmnode.model.User;
import com.project.farmnode.repo.FriendRepo;
import com.project.farmnode.repo.UserRepo;

import com.project.farmnode.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/friends/")
@AllArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final FriendRepo friendRepo;
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @GetMapping("/addFriend")
    public ResponseEntity<?> addUser(@RequestParam("friendId")String friendId, HttpServletRequest request) throws NullPointerException{
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        UserDto currentUser = userMapper.mapToDto(user);

        friendService.saveFriend(currentUser,Long.parseLong(friendId));
        return ResponseEntity.ok("Friend added successfully");
    }

    @GetMapping("/listFriends")
    public ResponseEntity<List<UserDto>> getFriends(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        List<UserDto> myFriends = friendService.getFriends(username);
        return new ResponseEntity<List<UserDto>>(myFriends, HttpStatus.OK);
    }

    @GetMapping("/checkFriend")
    public boolean  checkFriend(@RequestParam("friendId")String friendId, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        User user = userRepo.getOne(Long.parseLong(friendId));

        User user1 = userRepo.findByUsername(username);
        User user2 = userRepo.findByUsername(user.getUsername());
        User firstuser = user1;
        User seconduser = user2;
        if(user1.getUserId() > user2.getUserId()){
            firstuser = user2;
            seconduser = user1;
        }

        boolean friends = friendRepo.existsByFirstUserAndSecondUser(firstuser,seconduser);
        return friends;
    }
}
