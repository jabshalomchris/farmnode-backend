package com.project.farmnode.repository;

import com.project.farmnode.model.Friend;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepo extends JpaRepository<Friend, Long> {

    /*boolean existsByFirstUserAndSecondUser(User first, User second);
    List<Friend> findByFirstUser(User user);
    List<Friend> findBySecondUser(User user);*/

    boolean existsBySenderAndReceiver(User sender, User receiver);
    boolean existsBySenderAndReceiverAndApprovalStatus(User sender, User receiver,String status);
    boolean existsByReceiverAndSenderAndApprovalStatus(User sender, User receiver,String status);

    Friend findBySenderAndReceiver(User sender, User receiver);
    Friend findByReceiverAndSender(User sender, User receiver);

    List<Friend> findBySenderAndApprovalStatus(User user, String status);
    List<Friend> findByReceiverAndApprovalStatus(User user, String status);
}
