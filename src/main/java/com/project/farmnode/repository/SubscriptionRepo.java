package com.project.farmnode.repository;

import com.project.farmnode.model.Produce;
import com.project.farmnode.model.Subscription;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    boolean existsByUserAndProduce(User user, Produce produce);
    List<Subscription> findByUser(User user);
    List<Subscription> findByProduce(Produce produce);
    long deleteByUserAndProduce(User user, Produce produce);
}
