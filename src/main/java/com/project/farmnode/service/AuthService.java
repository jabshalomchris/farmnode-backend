package com.project.farmnode.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
