package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.FellowUserDto;
import com.project.farmnode.dto.UserDto;
import com.project.farmnode.enums.FriendshipStatus;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.FellowUserMapper;
import com.project.farmnode.mapper.UserMapper;
import com.project.farmnode.model.Friend;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.FriendRepo;
import com.project.farmnode.repository.UserRepo;

import com.project.farmnode.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
@AllArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final FriendRepo friendRepo;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final FellowUserMapper fellowUserMapper;

    @PostMapping("/addFriend/{friendId}")
    public ResponseEntity<ApiResponse> addUser(@PathVariable("friendId")String friendId, HttpServletRequest request) throws NullPointerException{
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        UserDto currentUser = userMapper.mapToDto(user);

        friendService.saveFriend(currentUser,Long.parseLong(friendId));
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Friend request sent successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/listFriends")
    public ResponseEntity<List<UserDto>> getFriends(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        List<UserDto> myFriends = friendService.getFriendsOfUser(username);
        return new ResponseEntity<List<UserDto>>(myFriends, HttpStatus.OK);
    }

    @GetMapping("/inbound-requests")
    public ResponseEntity<List<UserDto>> getInboundRequests(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        List<UserDto> myFriends = friendService.getInboundRequests(username);
        return new ResponseEntity<List<UserDto>>(myFriends, HttpStatus.OK);
    }

    @GetMapping("/outbound-requests")
    public ResponseEntity<List<UserDto>> getOutBoundRequests(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        List<UserDto> myFriends = friendService.getOutBoundRequests(username);
        return new ResponseEntity<List<UserDto>>(myFriends, HttpStatus.OK);
    }


    @GetMapping("/checkFriend")
    public String checkFriend(@RequestParam("id")String id, HttpServletRequest request) {
        String friendship;
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        User user = userRepo.getOne(Long.parseLong(id));

        User user1 = userRepo.findByUsername(username);
        User user2 = userRepo.findByUsername(user.getUsername());

        Friend friend = friendRepo.findBySenderAndReceiver(user1,user2);
        if(friend==null){
            friend = friendRepo.findByReceiverAndSender(user1,user2);
        }

        if(friend!=null){
            friendship=friend.getApprovalStatus();
        }
        else{
            friendship="false";
        }
        return friendship;
    }

    @GetMapping("/fellow-user")
    public FellowUserDto  checkFriendUser(@RequestParam("username")String username, HttpServletRequest request) {
        String friendship;
        Principal principal = request.getUserPrincipal();
        String currentusername = principal.getName();

        //User user = userRepo.getOne(Long.parseLong(fellowId));

        User user1 = userRepo.findByUsername(currentusername);
        User user2 = userRepo.findByUsername(username);

        Friend friend = friendRepo.findBySenderAndReceiver(user1,user2);

        if(friend==null){
            friend = friendRepo.findByReceiverAndSender(user1,user2);
        }

        if(friend!=null){
            friendship=friend.getApprovalStatus();
        }
        else{
            friendship="false";
        }
        FellowUserDto fellowUserDto = fellowUserMapper.mapToDto(user2,friendship);
        return fellowUserDto;
    }

    @PostMapping("/approve-friend/{sender_id}")
    public ResponseEntity<ApiResponse> approveFriendshipByReceiver(@PathVariable("sender_id")Long sender_id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        long receiver_id = userRepo.findByUsername(username).getUserId();
        //long sender_id= userRepo.findByUsername(username).getUserId();

        try{
            friendService.changeFriendStatus(FriendshipStatus.APPROVED.toString(),sender_id,receiver_id);
            }
        catch(Exception e){
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Acceptance failed"), HttpStatus.EXPECTATION_FAILED);

        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Friend request accepted successfully"), HttpStatus.OK);

    }

    @DeleteMapping("/cancel-request/{receiverId}")
    public ResponseEntity<ApiResponse> cancelRequestBySender(@PathVariable("receiverId")Long receiverId, HttpServletRequest request) throws NullPointerException{

        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        User sender = userRepo.findByUsername(username);


        User receiver = userRepo.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+receiverId));

        try{
            friendService.deleteRequest(sender,receiver);
        }
        catch(Exception exception){
            return new ResponseEntity<>(new ApiResponse(false, "Failed to cancel request "+exception), HttpStatus.EXPECTATION_FAILED);

        }
        return new ResponseEntity<>(new ApiResponse(true, "Request cancelled successfully"), HttpStatus.OK);

    }



    /*@GetMapping("/checkFriend")
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
    }*/
}
