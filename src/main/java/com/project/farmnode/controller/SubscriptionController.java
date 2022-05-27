package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.dto.UserDto;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import com.project.farmnode.repository.UserRepo;
import com.project.farmnode.service.PostService;
import com.project.farmnode.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/subscription")
@AllArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserRepo userRepo;
    private final ProduceRepo produceRepo;

    @RequestMapping(value = "/{produceId}",
            produces = "application/json",
            method=RequestMethod.POST)
    public ResponseEntity<ApiResponse> subscribe(@PathVariable("produceId")Long produceId, HttpServletRequest request) throws NullPointerException{
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Produce produce = produceRepo.findById(produceId)
                .orElseThrow(() -> new ResourceNotFoundException("Produce is not found with id: "+produceId));
        if(!subscriptionService.checkSubscription(user,produce)){
            try{
                subscriptionService.subscribe(user,produce);
            }
            catch(Exception exception){
                return new ResponseEntity<>(new ApiResponse(false, "Failed to subscribe: "+exception), HttpStatus.INTERNAL_SERVER_ERROR);

            }

            return new ResponseEntity<>(new ApiResponse(true, "You have subscribed to "
                    +user.getName()+"'s "+produce.getProduceName()+" successfully"), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(new ApiResponse(false, "You have already subscribed to "
                    +user.getName()+"'s "+produce.getProduceName()+""), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/unsubscribe/{produceId}")
    public ResponseEntity<ApiResponse> unsubscribe(@PathVariable("produceId")Long produceId, HttpServletRequest request) throws NullPointerException{
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Produce produce = produceRepo.findById(produceId)
                .orElseThrow(() -> new ResourceNotFoundException("Produce is not found with id: "+produceId));

        try{
            subscriptionService.unsubscribe(user,produce);
        }
        catch(Exception exception){
            return new ResponseEntity<>(new ApiResponse(false, "Failed to unsubscribe: "+exception), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(new ApiResponse(true, "You have unsubscribed "
                +user.getName()+"'s "+produce.getProduceName()+" successfully"), HttpStatus.CREATED);

    }

    @GetMapping("/by-user")
    public ResponseEntity<Map<String, List<ProduceDto>>>  findByUser(HttpServletRequest request) throws NullPointerException{
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        return status(HttpStatus.OK).body(subscriptionService.findByUser(username));
    }

    @GetMapping("/by-user-detailed")
    public ResponseEntity <List<ProduceDto>>  findByUserDetailed(HttpServletRequest request) throws NullPointerException{
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        return status(HttpStatus.OK).body(subscriptionService.findByUserDetailed(username));
    }

    @GetMapping("/check")
    public boolean checkSubscription(@RequestParam("produceId")Long produceId, HttpServletRequest request) throws NullPointerException{
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Produce produce = produceRepo.findById(produceId)
                .orElseThrow(() -> new ResourceNotFoundException("Produce is not found with id: "+produceId));

        return (subscriptionService.checkSubscription(user,produce));
    }

}
