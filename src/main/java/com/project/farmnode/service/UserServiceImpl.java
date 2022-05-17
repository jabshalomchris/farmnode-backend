package com.project.farmnode.service;

import com.project.farmnode.dto.RegisterRequest;
import com.project.farmnode.dto.ResponseDto;
import com.project.farmnode.enums.ResponseStatus;
import com.project.farmnode.exception.CustomException;
import com.project.farmnode.model.Role;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.RoleRepo;
import com.project.farmnode.repository.UserRepo;
import com.project.farmnode.utils.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    public ResponseDto signup(RegisterRequest registerRequest) {
        // Check to see if the current email address has already been registered.
        if (Helper.notNull(userRepo.findByUsername(registerRequest.getUsername()))) {
            // If the email address has been registered then throw an exception.
            throw new CustomException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreationDate(Instant.now());
        user.setActive(true); //set false after enabling email activation
        try {
            userRepo.save(user);
//        mailService.sendMail(new NotificationEmail("Please Activate your Account",
//                user.getUsername(), "Thank you for signing up to Farmnode, " +
//                "please click on the below url to activate your account : "));

        }
        catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
        return new ResponseDto(ResponseStatus.success.toString(), "User Registration Successful");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user==null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        /*user.getRoles().forEach(role-> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });*/
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
         //user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User getFellowUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }
}
