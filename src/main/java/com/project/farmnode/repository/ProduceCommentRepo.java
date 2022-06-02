package com.project.farmnode.repository;

import com.project.farmnode.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduceCommentRepo extends JpaRepository<ProduceComment, Long> {
    List<ProduceComment> findByProduceOrderByCreatedDateDesc(Produce produce);

    List<ProduceComment> findAllByUser(User user);

}
