package com.project.farmnode.service;

import com.project.farmnode.dto.RegisterRequest;
import com.project.farmnode.dto.ResponseDto;
import com.project.farmnode.model.Role;
import com.project.farmnode.model.User;

import java.util.List;

public interface UserService {

    ResponseDto signup(RegisterRequest registerRequest);
    User saveUser(User user);
    Role saveRole(Role role);
    //assuming no username duplicates
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    //in real world application we dont return all users but a page of users
    List<User> getUsers();
}
