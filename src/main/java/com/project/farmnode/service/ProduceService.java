package com.project.farmnode.service;

import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.dto.ProduceFilterDto;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.ProduceMapper;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import com.project.farmnode.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ProduceService {
    private final UserRepo userRepo;
    private final ProduceRepo produceRepo;
    private final UserService userService;
    private final ProduceMapper produceMapper;

    public void save(ProduceDto produceDto, String Username) {
        produceRepo.save(produceMapper.map(produceDto, userService.getUser(Username)));
    }

    @Transactional(readOnly = true)
    public List<ProduceDto> getAllProduce() {
        return produceRepo.findAll()
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public ProduceDto getProduce(Long id) {
        Produce produce = produceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produce is not found with id: "+id.toString()));
        return produceMapper.mapToDto(produce);
        //return produce;
    }

    @Transactional(readOnly = true)
    public List<ProduceDto> getProduceByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
       //              .orElseThrow(() -> new UsernameNotFoundException(userName));
        return produceRepo.findByUser(user)
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());
    }

    public List<ProduceDto> getFilteredProduces(ProduceFilterDto produceFilterDto) {
        String sw_lat = produceFilterDto.getSw_lat();
        String ne_lat = produceFilterDto.getNe_lat();
        String sw_lng = produceFilterDto.getSw_lng();
        String ne_lng = produceFilterDto.getNe_lng();

        return produceRepo.findByFilters(sw_lat,ne_lat, sw_lng, ne_lng)
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());
    }

    public List<ProduceDto> getFilteredGeoJsonProduces(String sw_lat, String ne_lat, String sw_lng, String ne_lng) {

        return produceRepo.findByFilters(sw_lat,ne_lat, sw_lng, ne_lng)
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());
    }




}
