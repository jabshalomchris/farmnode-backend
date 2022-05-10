package com.project.farmnode.repo;

import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduceRepo extends JpaRepository<Produce, Long> {
    List<Produce> findByUser(User user);
}
