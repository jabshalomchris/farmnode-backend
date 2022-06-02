package com.project.farmnode.repository;

import com.project.farmnode.dto.ProduceRequest.RequestDbDto;
import com.project.farmnode.model.Request;
import com.project.farmnode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {

    @Query(value = "SELECT \n" +
            "created_date,request_status,buyer_id,grower_id,message,i.price,quantity,i.produce_id,r.request_id,produce_name FROM request r join request_item i on r.request_id=i.request_id join produce p on i.produce_id=p.produce_id where  r.buyer_id = ?1 ORDER BY created_date desc;",
            nativeQuery = true)
    List<RequestDbDto> findallbybuyer(long buyerId);

    List<Request> findAllByBuyerIdOrderByCreatedDateDesc(User buyer);

    List<Request> findAllByGrowerIdOrderByCreatedDateDesc(User grower);

    @Modifying
    @Query(value = "UPDATE request SET request_status = ?1 WHERE (request_id = ?2)",
            nativeQuery = true)
    void updateRequestStatus(String status,int requestId);
}
