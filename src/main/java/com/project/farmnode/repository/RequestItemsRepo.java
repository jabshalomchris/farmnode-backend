package com.project.farmnode.repository;

import com.project.farmnode.model.RequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestItemsRepo extends JpaRepository<RequestItem, Long> {
}
