package com.project.farmnode.service;

import com.project.farmnode.dto.GeoJsonBuilder.feature.FeatureCollectionBuilder;
import com.project.farmnode.dto.GeoJsonModel.feature.FeatureCollectionDto;
import com.project.farmnode.dto.GeoJsonModel.feature.FeatureDto;
import com.project.farmnode.dto.GeoJsonModel.geometry.PointDto;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.enums.ProduceStatus;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.ProduceMapper;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import com.project.farmnode.repository.SubscriptionRepo;
import com.project.farmnode.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
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
    private final SubscriptionRepo subscriptionRepo;

    /*public Produce save(ProduceDto produceDto, String Username) {
        User user = userService.getUser(Username);
        return produceRepo.save(produceMapper.map(produceDto, user));
    }*/
    public void save(Produce produce, String Username) {
        User user = userService.getUser(Username);
        produce.setUser(user);
        produceRepo.save(produce);
    }

    public void update(ProduceDto produceDto, String Username) {
        produceRepo.save(produceMapper.mapUpdate(produceDto,userService.getUser(Username)));
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
                .orElseThrow(() -> new ResourceNotFoundException("Produce is not found with id: "+id));
        return produceMapper.mapToDto(produce);
        //return produce;
    }

    @Transactional(readOnly = true)
    public ProduceDto getProduceWithSubscription(Long id, String username) {
        User user = userRepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        Produce produce = produceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produce is not found with id: "+id));

        ProduceDto produceDto = new ProduceDto();
        produceDto.setProduceId( produce.getProduceId() );
        produceDto.setUserName( produce.getUser().getUsername() );
        produceDto.setProduceName( produce.getProduceName() );
        produceDto.setDescription( produce.getDescription() );
        produceDto.setProduceStatus( produce.getProduceStatus() );
        produceDto.setPrice( produce.getPrice() );
        produceDto.setCategory( produce.getCategory() );
        produceDto.setAddress( produce.getAddress() );
        produceDto.setMeasureType( produce.getMeasureType() );
        produceDto.setFilename( produce.getFilename() );
        produceDto.setLongitude( produce.getLongitude() );
        produceDto.setLatitude( produce.getLatitude() );
        produceDto.setPublishStatus( produce.getPublishStatus() );
        produceDto.setSubscription( subscriptionRepo.existsByUserAndProduce(user,produce) );

        return produceDto;
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

    @Transactional(readOnly = true)
    public List<ProduceDto> getProduceByUserId(Long userId) {
        User user = userRepo.getById(userId);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        //              .orElseThrow(() -> new UsernameNotFoundException(userName));
        return produceRepo.findByUser(user)
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<ProduceDto> getProduceByUserIdForRequest(Long userId) {

        //              .orElseThrow(() -> new UsernameNotFoundException(userName));
        return produceRepo.findByUserForRequest(userId)
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<ProduceDto> getProduceByUsernameAndPublishStatus(String username, String publishStatus) {
        User user = userRepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        //              .orElseThrow(() -> new UsernameNotFoundException(userName));
        return produceRepo.findByUserAndPublishStatus(user,publishStatus)
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());
    }



    /*public List<ProduceDto> getFilteredProduces(ProduceFilterDto produceFilterDto) {
        String sw_lat = produceFilterDto.getSw_lat();
        String ne_lat = produceFilterDto.getNe_lat();
        String sw_lng = produceFilterDto.getSw_lng();
        String ne_lng = produceFilterDto.getNe_lng();

        return produceRepo.findByFilters(sw_lat,ne_lat, sw_lng, ne_lng,"","")
                .stream()
                .map(produceMapper::mapToDto)
                .collect(toList());}
    }*/

    public List<ProduceDto> getFilteredProduces(String sw_lat, String ne_lat, String sw_lng, String ne_lng,String category, String status, String username) {


        if(username==""){
            return produceRepo.findByFilters(sw_lat,ne_lat, sw_lng, ne_lng,category,status)
                    .stream()
                    .map(produceMapper::mapToDto)
                    .collect(toList());
        }
        else{
            Long userId = userRepo.findByUsername(username).getUserId();
            return produceRepo.findByFiltersOfNonusersProduce(sw_lat,ne_lat, sw_lng, ne_lng,category, status, userId)
                    .stream()
                    .map(produceMapper::mapToDto)
                    .collect(toList());
        }
    }



    public String getFilteredGeoJsonProduces(String sw_lat, String ne_lat, String sw_lng, String ne_lng,String category, String status, String username) {

        FeatureCollectionDto featureCollection = new FeatureCollectionDto();
        //creating a list of features
        List<FeatureDto> dtoList = new ArrayList<>();
        List<ProduceDto > produceDtoList;

        if(username==""){
            produceDtoList =produceRepo.findByFilters(sw_lat,ne_lat, sw_lng, ne_lng,category,status)
                    .stream()
                    .map(produceMapper::mapToDto)
                    .collect(toList());
        }
        else{
            produceDtoList=produceRepo.findByFiltersOfNonusersProduce(sw_lat,ne_lat, sw_lng, ne_lng,category, status, userRepo.findByUsername(username).getUserId())
                    .stream()
                    .map(produceMapper::mapToDto)
                    .collect(toList());

        }


        for (ProduceDto element : produceDtoList) {
            FeatureDto feature = new FeatureDto();
            PointDto point = new PointDto(element.getLongitude(), element.getLatitude() );
            feature.setGeometry(point);
            feature.setId(element.getProduceId().toString());
            feature.setProperties
                    ("{\"Name\": \""+element.getProduceName()+"\", "+
                            "\"description\": \""+element.getDescription()+"\", "+
                            "\"produceStatus\": \""+element.getProduceStatus()+"\", "+
                            "\"price\": \""+element.getPrice()+"\", "+
                            "\"category\": \""+element.getCategory()+"\", "+
                            "\"measureType\": \""+element.getMeasureType()+"\", "+
                            "\"address\": \""+element.getAddress()+"\", "+
                            "\"grower\": \""+element.getUserName()+"\", "+
                            "\"filename\": \""+element.getFilename()+"\", "+
                            "\"publishStatus\": \""+element.getPublishStatus()+"\" "+"}");

            dtoList.add(feature);
        }
        featureCollection.setFeatures(dtoList);

        // String featureCollectionGeoJSON = FeatureCollectionBuilder.getInstance().toGeoJSON(featureCollection);

        return FeatureCollectionBuilder.getInstance().toGeoJSON(featureCollection);
    }


    public void updateProduceStatus(String status,int produceId) {
        String produceStatus;

        if(status =="RIPE"){
            produceStatus = ProduceStatus.RIPE.responsibleState();
        }
        else{
            produceStatus = ProduceStatus.GROWING.responsibleState();
        }
        produceRepo.updateProduceStatus(status,produceId);
    }
    public void updatePublishStatus(String status,int produceId) {
        produceRepo.updatePublishStatus(status,produceId);
    }

}
