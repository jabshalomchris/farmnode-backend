package com.project.farmnode.repository;

import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ProduceRepoTest {

    @Autowired
    private ProduceRepo repoUnderTest;

    @Autowired
    private UserRepo userRepo;

    @AfterEach
    void tearDown(){
       // repoUnderTest.deleteAll();
    }


    @Test
    void findByExistingUser() {
        //given
        User user = new User(1L,"Testing","testing@test.com","123","file","123", Instant.now(),true);
        userRepo.save(user);

        Produce produce= new Produce(
                1L,"test","sds","RIPE",1.00,"ssd","sds","sds",1.0,1.0,"ds","dd",user);

        repoUnderTest.save(produce);

        //then
        assertThat(repoUnderTest.findByUser(user)).isNotNull();
    }


    @Test
    void findByUserAndPublishStatus() {

        //given
        User user1 = new User(null,"Testing","testing@test.com","123","false","123", Instant.now(),true);
        userRepo.save(user1);

        Produce produce1= new Produce(
                null,"test","sds","RIPE",1.00,"ssd","sds","sds",1.0,1.0,"false","true",user1);

        repoUnderTest.save(produce1);

        Produce produce2= new Produce(
                null,"test","sds","RIPE",1.00,"ssd","sds","sds",1.0,1.0,"false","false",user1);

        repoUnderTest.save(produce2);
        List<Produce> expectedResult = new ArrayList<>();
        expectedResult.add(produce1);
        List<Produce> result = repoUnderTest.findByUserAndPublishStatus(user1,"true");
        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void findByUserForRequest() {
        //given
        User user = new User(null,"Testing","testing@test.com","123","false","123", Instant.now(),true);
        userRepo.save(user);

        Produce produce1= new Produce(
                null,"test","sds","RIPE",1.00,"ssd","sds","sds",1.0,1.0,"false","true",user);


        assertNotNull(repoUnderTest.findByUserForRequest(4L));
    }

    @Test
    void findByFilters() {
    }

    @Test
    @Disabled
    void findByFiltersOfNonusersProduce() {
    }

    @Test
    void updateStatusToRipe() {

        User user = new User(null,"Testing","testing@test.com","123","false","123", Instant.now(),true);
        userRepo.save(user);
        //given
        Produce produce5= new Produce(
                null,"test","sds","RIPE",1.00,"ssd","sds","sds",1.0,1.0,"false","true",user);

        repoUnderTest.save(produce5);

        //when
        String newStatus ="RIPE";
        repoUnderTest.updateProduceStatus(newStatus,produce5.getProduceId().intValue());

        Produce result = repoUnderTest.getById(produce5.getProduceId());
        String resultStatus="";

        //then
        assertEquals(result.getProduceStatus(),newStatus);

    }

    @Test
    void updateStatusToGrowing() {

        User user = new User(null,"Testing","testing@test.com","123","false","123", Instant.now(),true);
        userRepo.save(user);
        //given
        Produce produce5= new Produce(
                null,"test","sds","GROWING",1.00,"ssd","sds","sds",1.0,1.0,"false","true",user);

        repoUnderTest.save(produce5);

        //when
        String newStatus ="GROWING";
        repoUnderTest.updateProduceStatus(newStatus,produce5.getProduceId().intValue());

        Produce result = repoUnderTest.getById(produce5.getProduceId());
        String resultStatus="";

        //then
        assertEquals(result.getProduceStatus(),newStatus);

    }


    @Test
    void updatePublishStatusToTrue() {
        User user111 = new User(null,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        userRepo.save(user111);
        //given
        Produce produce5= new Produce(
                null,"test","sds","GROWING",1.00,"ssd","sds","sds",1.0,1.0,"false","false",user111);
        repoUnderTest.save(produce5);

        repoUnderTest.updatePublishStatus("true",produce5.getProduceId().intValue());

        Produce result = repoUnderTest.getById(produce5.getProduceId());

        //then
        assertEquals(result.getPublishStatus(),"true");
    }

    @Test
    void updatePublishStatusToFalse() {
        User user111 = new User(null,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);
        userRepo.save(user111);
        //given
        Produce produce5= new Produce(
                null,"test","sds","GROWING",1.00,"ssd","sds","sds",1.0,1.0,"false","true",user111);
        repoUnderTest.save(produce5);

        repoUnderTest.updatePublishStatus("false",produce5.getProduceId().intValue());

        Produce result = repoUnderTest.getById(produce5.getProduceId());

        //then
        assertEquals(result.getPublishStatus(),"false");
    }
}