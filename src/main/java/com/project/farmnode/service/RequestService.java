package com.project.farmnode.service;


import com.project.farmnode.dto.ProduceRequest.RequestDto;
import com.project.farmnode.dto.ProduceRequest.RequestItemResponseDto;
import com.project.farmnode.dto.ProduceRequest.RequestItemsDto;
import com.project.farmnode.dto.ProduceRequest.RequestResponseDto;
import com.project.farmnode.enums.RequestStatus;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.Request;
import com.project.farmnode.model.RequestItem;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import com.project.farmnode.repository.RequestItemsRepo;
import com.project.farmnode.repository.RequestRepo;
import com.project.farmnode.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RequestService {
    private final RequestRepo requestRepo;
    private final UserRepo userRepo;
    private final ProduceRepo produceRepo;
    private final RequestItemsRepo requestItemsRepo;
    private final UserService userService;

    public void save(RequestDto requestDto,String Username) {
        User buyer = userService.getUser(Username);
        User grower = userRepo.findById(requestDto.getGrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+requestDto.getGrowerId()));

        Request newRequest = new Request();
        newRequest.setRequestStatus(RequestStatus.PENDING.toString());
        newRequest.setMessage(requestDto.getMessage());
        newRequest.setGrowerId(grower);
        newRequest.setBuyerId(buyer);
        newRequest.setCreatedDate(java.time.Instant.now());
        requestRepo.save(newRequest);

        List<RequestItemsDto> requestItemsDtoList = requestDto.getRequestItem();
        for (RequestItemsDto requestItemsDto : requestItemsDtoList) {

            Produce produce = produceRepo.findById(requestItemsDto.getProduceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produce is not found with id: "));
            // create orderItem and save each one
            RequestItem requestItem = new RequestItem();
            requestItem.setRequest(newRequest);
            requestItem.setPrice(requestItemsDto.getLinetotal());
            requestItem.setQuantity(requestItemsDto.getQuantity());
            requestItem.setProduce(produce);

            // add to order item list
            requestItemsRepo.save(requestItem);
        }
    }

    public List<RequestResponseDto> getRequestsByBuyer(String username){
        User buyer = userRepo.findByUsername(username);
        if(buyer==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        List<Request>  requestList = requestRepo.findAllByBuyerIdOrderByCreatedDateDesc(buyer);
        List<RequestItem> requestItemsList;


        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();


        for (Request element : requestList) {
            RequestResponseDto requestResponseDto = new RequestResponseDto();
            List<RequestItemResponseDto> requestItemResponseDtoList = new ArrayList<>();

            requestResponseDto.setRequestId(element.getRequestId());
            requestResponseDto.setGrowerId(element.getGrowerId().getUserId());
            requestResponseDto.setGrowerName(element.getGrowerId().getName());
            requestResponseDto.setBuyerId(element.getBuyerId().getUserId());
            requestResponseDto.setBuyerName(element.getBuyerId().getName());
            requestResponseDto.setMessage(element.getMessage());

            Timestamp timestamp = Timestamp.from(element.getCreatedDate());

            String pattern = " dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date(timestamp.getTime()));
            requestResponseDto.setCreatedDate(date);



            if(element.getRequestStatus()!=null){
                requestResponseDto.setRequestStatus(element.getRequestStatus());
            }
            else{
                requestResponseDto.setRequestStatus("");
            }

            requestItemsList = element.getRequestItem();

            for (RequestItem itemElement : requestItemsList){
                RequestItemResponseDto requestItemResponseDto = new RequestItemResponseDto();
                requestItemResponseDto.setProduceId(itemElement.getProduce().getProduceId());
                requestItemResponseDto.setProduceName(itemElement.getProduce().getProduceName());
                requestItemResponseDto.setQuantity(itemElement.getQuantity());
                requestItemResponseDto.setPrice(itemElement.getPrice());
                requestItemResponseDto.setLinetotal(itemElement.getPrice()*itemElement.getQuantity());
                requestItemResponseDto.setFileName(itemElement.getProduce().getFilename());
                requestItemResponseDtoList.add(requestItemResponseDto);
            }

            requestResponseDto.setRequestItem(requestItemResponseDtoList);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    public List<RequestResponseDto> getRequestsByGrower(String username){

        User grower = userRepo.findByUsername(username);
        if(grower==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        List<Request>  requestList = requestRepo.findAllByGrowerIdOrderByCreatedDateDesc(grower);
        List<RequestItem> requestItemsList;

        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();


        for (Request element : requestList) {
            RequestResponseDto requestResponseDto = new RequestResponseDto();
            List<RequestItemResponseDto> requestItemResponseDtoList = new ArrayList<>();

            requestResponseDto.setRequestId(element.getRequestId());
            requestResponseDto.setGrowerId(element.getGrowerId().getUserId());
            requestResponseDto.setGrowerName(element.getGrowerId().getName());
            requestResponseDto.setBuyerId(element.getBuyerId().getUserId());
            requestResponseDto.setBuyerName(element.getBuyerId().getName());
            requestResponseDto.setMessage(element.getMessage());
            Timestamp timestamp = Timestamp.from(element.getCreatedDate());

            Date date = new Date(timestamp.getTime());
            requestResponseDto.setCreatedDate(date.toString());


            if(element.getRequestStatus()!=null){
                requestResponseDto.setRequestStatus(element.getRequestStatus());
            }
            else{
                requestResponseDto.setRequestStatus("");
            }

            requestItemsList = element.getRequestItem();

            for (RequestItem itemElement : requestItemsList){
                RequestItemResponseDto requestItemResponseDto = new RequestItemResponseDto();
                requestItemResponseDto.setProduceId(itemElement.getProduce().getProduceId());
                requestItemResponseDto.setProduceName(itemElement.getProduce().getProduceName());
                requestItemResponseDto.setQuantity(itemElement.getQuantity());
                requestItemResponseDto.setPrice(itemElement.getPrice());
                requestItemResponseDto.setLinetotal(itemElement.getPrice()*itemElement.getQuantity());
                requestItemResponseDto.setFileName(itemElement.getProduce().getFilename());
                requestItemResponseDtoList.add(requestItemResponseDto);
            }
            requestResponseDto.setRequestItem(requestItemResponseDtoList);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;

    }

    public RequestResponseDto getRequestById(Long requestId) throws ResourceNotFoundException {
        List<RequestItem> requestItemsList;
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: "+requestId));

        RequestResponseDto requestResponseDto = new RequestResponseDto();
        List<RequestItemResponseDto> requestItemResponseDtoList = new ArrayList<>();

        requestResponseDto.setRequestId(request.getRequestId());
        requestResponseDto.setGrowerId(request.getGrowerId().getUserId());
        requestResponseDto.setGrowerName(request.getGrowerId().getName());
        requestResponseDto.setBuyerId(request.getBuyerId().getUserId());
        requestResponseDto.setBuyerName(request.getBuyerId().getName());
        requestResponseDto.setMessage(request.getMessage());
        Timestamp timestamp = Timestamp.from(request.getCreatedDate());

        Date date = new Date(timestamp.getTime());
        requestResponseDto.setCreatedDate(date.toString());


        if(request.getRequestStatus()!=null){
            requestResponseDto.setRequestStatus(request.getRequestStatus());
        }
        else{
            requestResponseDto.setRequestStatus("");
        }

        requestItemsList = request.getRequestItem();

        for (RequestItem itemElement : requestItemsList){
            RequestItemResponseDto requestItemResponseDto = new RequestItemResponseDto();
            requestItemResponseDto.setProduceId(itemElement.getProduce().getProduceId());
            requestItemResponseDto.setProduceName(itemElement.getProduce().getProduceName());
            requestItemResponseDto.setQuantity(itemElement.getQuantity());
            requestItemResponseDto.setPrice(itemElement.getPrice());
            requestItemResponseDto.setLinetotal(itemElement.getPrice()*itemElement.getQuantity());
            requestItemResponseDto.setFileName(itemElement.getProduce().getFilename());
            requestItemResponseDtoList.add(requestItemResponseDto);
        }
        requestResponseDto.setRequestItem(requestItemResponseDtoList);
        return requestResponseDto;

    }

    public void updateRequestStatus(int requestId,String status) {
        String requestStatus;

        if(status =="APPROVED"){
            requestStatus = RequestStatus.APPROVED.toString();
        }
        else if(status =="DECLINED"){
            requestStatus = RequestStatus.DECLINED.toString();
        }
        else{
            requestStatus = RequestStatus.PENDING.toString();
        }
        requestRepo.updateRequestStatus(requestStatus,requestId);
    }
}
