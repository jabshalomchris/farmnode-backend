package com.project.farmnode.service;

import com.project.farmnode.dto.GeoJsonModel.feature.FeatureDto;
import com.project.farmnode.dto.GeoJsonModel.geometry.PointDto;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.dto.SubscriptionDto;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.ProduceMapper;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.Subscription;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import com.project.farmnode.repository.SubscriptionRepo;
import com.project.farmnode.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SubscriptionService {
    private final UserRepo userRepo;
    private final UserService userService;
    private final ProduceRepo produceRepo;
    private final SubscriptionRepo subscriptionRepo;
    private final ProduceMapper produceMapper;


    public void subscribe(User user, Produce produce) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setProduce(produce);
        subscriptionRepo.save(subscription);
    }

    public void unsubscribe(User user, Produce produce){

        subscriptionRepo.deleteByUserAndProduce(user,produce);
    }

    public boolean checkSubscription(User user, Produce produce){
        return(subscriptionRepo.existsByUserAndProduce(user,produce));
    }

    public Map<String, List<ProduceDto>> findByUser(String username){
        User user = userRepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        List<ProduceDto> produceDtoList = new ArrayList<>();
        List<Subscription> subscriptionList = subscriptionRepo.findByUser(user);

        for (Subscription element : subscriptionList) {

            Produce produce = element.getProduce();
            ProduceDto produceDto = produceMapper.mapToDto(produce);
            produceDtoList.add(produceDto);
        }

        ///USE IN THEORY
        /*Simply use Collectors::groupingBy and pass method reference to MyDto::getName method which will be used as the key for your map:*/
        //List<MyDto> dtos = ...
        Map<String, List<ProduceDto>> map = produceDtoList.stream()
                .collect(Collectors.groupingBy(ProduceDto::getGrower));
        return map;
    }

    public List<ProduceDto> findByUserDetailed(String username){
        User user = userRepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        List<ProduceDto> produceDtoList = new ArrayList<>();
        List<Subscription> subscriptionList = subscriptionRepo.findByUser(user);

        for (Subscription element : subscriptionList) {

            Produce produce = element.getProduce();
            ProduceDto produceDto = produceMapper.mapToDto(produce);
            produceDtoList.add(produceDto);
        }
        return produceDtoList;
    }
}
