package com.project.farmnode.repository;

import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduceRepo extends JpaRepository<Produce, Long> {
    List<Produce> findByUser(User user);
    List<Produce> findByUserAndPublishStatus(User user, String publishStatus);

    @Query(value = "SELECT * FROM produce WHERE latitude > ?1 AND latitude < ?2 AND longitude > ?3 AND longitude < ?4",
            nativeQuery = true)
    List<Produce> findByFilters(String sw_lat,String ne_lat,String sw_lng,String ne_lng);


}
