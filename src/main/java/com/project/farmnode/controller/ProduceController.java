package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.PostRequest;
import com.project.farmnode.dto.PostResponse;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.dto.ProduceFilterDto;
import com.project.farmnode.model.Produce;
import com.project.farmnode.service.ProduceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/produce/")
@AllArgsConstructor
public class ProduceController {
    private final ProduceService produceService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProduce(@RequestBody ProduceDto produceDto, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        produceService.save(produceDto, username);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Produce has been added"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProduceDto>> getAllProduces() {
        return status(HttpStatus.OK).body(produceService.getAllProduce());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduceDto> getProduce(@PathVariable Long id) {
        return status(HttpStatus.OK).body(produceService.getProduce(id));
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<List<ProduceDto>> getProduceByUsername(@PathVariable String username) {
        return status(HttpStatus.OK).body(produceService.getProduceByUsername(username));
    }

    //fetch produces within bounds
    @GetMapping("by-filters")
    public ResponseEntity<List<ProduceDto>> getProduceByFilters(@RequestBody ProduceFilterDto produceFilterDto) {
        return status(HttpStatus.OK).body(produceService.getFilteredProduces(produceFilterDto));
    }

}
