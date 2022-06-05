package com.project.farmnode.repository;

import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduceRepo extends JpaRepository<Produce, Long> {
    List<Produce> findByUser(User user);

    List<Produce> findByUserAndPublishStatus(User user, String publishStatus);

    @Query(value = "SELECT * FROM produce WHERE user_id=?1 AND publish_status='true' AND produce_status='RIPE' ",
            nativeQuery = true)
    List<Produce> findByUserForRequest(Long userId);

    @Query(value = "SELECT * FROM produce WHERE latitude > ?1 AND latitude < ?2 AND longitude > ?3 AND longitude < ?4 AND category like %?5% AND produce_status like %?6%  AND publish_status='true'",
            nativeQuery = true)
    List<Produce> findByFilters(String sw_lat,String ne_lat,String sw_lng,String ne_lng, String category,String status);

    @Query(value = "SELECT * FROM produce WHERE latitude > ?1 AND latitude < ?2 AND longitude > ?3 AND longitude < ?4 AND category like %?5% AND produce_status like %?6% AND user_id != ?7 AND publish_status='true'",
            nativeQuery = true)
    List<Produce> findByFiltersOfNonusersProduce(String sw_lat,String ne_lat,String sw_lng,String ne_lng,String category, String status, long user_id);

 /*   @Query(value = "SELECT * FROM produce WHERE latitude > ?1 AND latitude < ?2 AND longitude > ?3 AND longitude < ?4 AND user_id != ?5",
            nativeQuery = true)
    List<Produce> findByFiltersWithoutGrower(String sw_lat,String ne_lat,String sw_lng,String ne_lng, int uid);*/

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE produce SET produce_status = ?1 WHERE (produce_id = ?2)",
            nativeQuery = true)
    void updateProduceStatus(String status, int pid);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE produce SET publish_status = ?1 WHERE (produce_id = ?2)",
            nativeQuery = true)
    void updatePublishStatus(String status, int pid);
}
