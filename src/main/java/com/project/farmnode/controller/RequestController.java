package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.dto.ProduceRequest.RequestDto;
import com.project.farmnode.dto.ProduceRequest.RequestItemsDto;

import com.project.farmnode.dto.ProduceRequest.RequestResponseDto;
import com.project.farmnode.model.Request;
import com.project.farmnode.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/request")
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public void createRequest(@RequestBody RequestDto requestDto, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        requestService.save(requestDto,username);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestResponseDto> getRequestsById(@PathVariable("requestId") long requestId) {
        return status(HttpStatus.OK).body(requestService.getRequestById(requestId));
    }

    @GetMapping("/by-buyer")
    public ResponseEntity<List<RequestResponseDto>> getRequestsByBuyer(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return status(HttpStatus.OK).body(requestService.getRequestsByBuyer(username));
    }

    @GetMapping("/by-grower")
    public ResponseEntity<List<RequestResponseDto>> getRequestsByGrower(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return status(HttpStatus.OK).body(requestService.getRequestsByGrower(username));
    }

    @PutMapping("/{requestId}/{status}")
    public ResponseEntity<ApiResponse> getProduceByUsernameAndStatus(@PathVariable("requestId") int requestId,
                                                                          @PathVariable("status") String status) {
        requestService.updateRequestStatus(requestId,status);
        return new ResponseEntity<>(new ApiResponse(true, "Request status updated"), HttpStatus.CREATED);
    }
}
