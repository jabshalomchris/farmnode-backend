package com.project.farmnode.repository;

import com.project.farmnode.model.Request;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {
    List<Request> findAllByUserOrderByCreatedDateDesc(User user);
}
