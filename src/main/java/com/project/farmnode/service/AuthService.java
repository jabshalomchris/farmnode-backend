package com.project.farmnode.service;

import com.project.farmnode.model.User;
import com.auth0.jwt.JWT;
import com.project.farmnode.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    /*private final UserRepo userRepo;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        JWT principal = (JWT) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepo.findByUsername(principal.getSubject());
                //.orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }*/
}
