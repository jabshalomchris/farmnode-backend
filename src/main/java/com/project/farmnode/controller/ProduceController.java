package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.dto.ProduceFilterDto;
import com.project.farmnode.model.Produce;
import com.project.farmnode.service.ProduceService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/produce")
@AllArgsConstructor
public class ProduceController {
    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads/produce";
    private final ProduceService produceService;

    /*@PostMapping
    public ResponseEntity<ApiResponse> createProduce(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        String newFile;
        do{
            newFile=generateUniqueFileName() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        }
        while(new File(uploadDirectory+newFile).exists());

        Path filenameAndPath = Paths.get(uploadDirectory, newFile );
        try{
            Files.write(filenameAndPath,file.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        ProduceDto produceDto = new ProduceDto();



        produceDto.setFilename(newFile);

        produceService.save(produceDto, username);
        return new ResponseEntity<>(new ApiResponse(true, "Produce has been added"), HttpStatus.CREATED);
    }*/

    @PostMapping
    public ResponseEntity<ApiResponse> createProduce(@RequestParam("produceName") String produceName,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("produceStatus") String produceStatus,
                                                     @RequestParam("price") String price,
                                                     @RequestParam("category") String category,
                                                     @RequestParam("measureType") String measureType,
                                                     @RequestParam("longitude") String longitude,
                                                     @RequestParam("latitude") String latitude,
                                                     @RequestParam("publishStatus") String publishStatus,
            @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        String newFile="";
        if(file!=null){

            do{
                newFile=generateUniqueFileName() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            }
            while(new File(uploadDirectory+newFile).exists());

            Path filenameAndPath = Paths.get(uploadDirectory, newFile );
            try{
                Files.write(filenameAndPath,file.getBytes());
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
        Produce produce = new Produce();
        produce.setProduceName(produceName);
        produce.setCategory(category);
        produce.setDescription(description);
        produce.setPrice(Double.valueOf(price));
        produce.setMeasureType(measureType);
        produce.setLatitude(Double.valueOf(latitude));
        produce.setLongitude(Double.valueOf(longitude));
        produce.setProduceStatus(produceStatus);
        produce.setPublishStatus(publishStatus);
        produce.setFilename(newFile);


       /* ProduceDto produceDto = new ProduceDto();
        produceDto.setProduceName(produceName);
        produceDto.setCategory(category);
        produceDto.setDescription(description);
        produceDto.setPrice(Double.valueOf(price));
        produceDto.setMeasureType(measureType);
        produceDto.setLatitude(Double.valueOf(latitude));
        produceDto.setLongitude(Double.valueOf(longitude));
        produceDto.setProduceStatus(produceStatus);
        produceDto.setPublishStatus(publishStatus);
        produceDto.setFilename(newFile);*/

        produceService.save(produce,username);
        return new ResponseEntity<>(new ApiResponse(true, "Produce has been added"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update",
            produces = "application/json",
            method=RequestMethod.PUT)
    public ResponseEntity<ApiResponse> updateProduce(@RequestBody ProduceDto produceDto,HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        produceService.update(produceDto,username);
        return new ResponseEntity<>(new ApiResponse(true, "Produce updated"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProduceDto>> getAllProduces() {
        return status(HttpStatus.OK).body(produceService.getAllProduce());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduceDto> getProduce(@PathVariable Long id) {
        return status(HttpStatus.OK).body(produceService.getProduce(id));
    }

    @GetMapping("/detailed/{id}")
    public ResponseEntity<ProduceDto> getProduceWithSubscription(@PathVariable Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return status(HttpStatus.OK).body(produceService.getProduceWithSubscription(id,username));
    }

    /*@GetMapping("/by-user/{username}")
    public ResponseEntity<List<ProduceDto>> getProduceByUsername(@PathVariable String username) {
        return status(HttpStatus.OK).body(produceService.getProduceByUsername(username));
    }*/

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ProduceDto>> getProduceByUserId(@PathVariable Long userId) {
        return status(HttpStatus.OK).body(produceService.getProduceByUserId(userId));
    }

    @GetMapping("/by-user/request/{userId}")
    public ResponseEntity<List<ProduceDto>> getProduceForRequestByUserId(@PathVariable Long userId) {
        return status(HttpStatus.OK).body(produceService.getProduceByUserIdForRequest(userId));
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<ProduceDto>> getProduceByUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return status(HttpStatus.OK).body(produceService.getProduceByUsername(username));
    }

    @GetMapping("/by-user/{username}/{status}")
    public ResponseEntity<List<ProduceDto>> getProduceByUsernameAndStatus(@PathVariable("username") String username,
                                                                          @PathVariable("status") String status) {
        return status(HttpStatus.OK).body(produceService.getProduceByUsernameAndPublishStatus(username,status));
    }

    @RequestMapping(value = "/update-status/{status}/{produceId}",
            produces = "application/json",
            method=RequestMethod.POST)
    public ResponseEntity<ApiResponse> updateStatusProduce(@PathVariable(value = "status") String status, @PathVariable(value = "produceId") int produceId) {
        produceService.updateProduceStatus(status, produceId);
        return new ResponseEntity<>(new ApiResponse(true, "Produce status updated"), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/update-publish/{status}/{produceId}",
            produces = "application/json",
            method=RequestMethod.POST)
    public ResponseEntity<ApiResponse> updatePublishStatusProduce(@PathVariable(value = "status") String status, @PathVariable(value = "produceId") int produceId) {
        produceService.updatePublishStatus(status, produceId);
        return new ResponseEntity<>(new ApiResponse(true, "Produce status updated"), HttpStatus.CREATED);
    }

    /*//fetch produces within bounds
    @GetMapping("/by-filters")
    public ResponseEntity<List<ProduceDto>> getProduceByFilters(@RequestBody ProduceFilterDto produceFilterDto) {
        return status(HttpStatus.OK).body(produceService.getFilteredProduces(produceFilterDto));
    }*/

    @RequestMapping(value = "/get-geojson", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getProduceGeoJsonByFiltersNewNew(@RequestParam String sw_lat,
                                                   @RequestParam String ne_lat,
                                                   @RequestParam String sw_lng,
                                                   @RequestParam String ne_lng,
                                                   @RequestParam String category,
                                                   @RequestParam String status,
                                                   @RequestParam boolean include_users,
                                                   HttpServletRequest request) {




        if(include_users == true){
            return produceService.getFilteredGeoJsonProduces(sw_lat,  ne_lat,  sw_lng,  ne_lng,category,status,"");
        }
        else{
            Principal principal = request.getUserPrincipal();
            String username = principal.getName();
            return produceService.getFilteredGeoJsonProduces(sw_lat,  ne_lat,  sw_lng,  ne_lng,category,status,username);
        }

    }


    /*@GetMapping("/geoJson")
    public String getProduceGeoJsonByFilters() {

        FeatureDto feature = new FeatureDto();
        PointDto point = new PointDto(79.86843824386597, 6.963606397102782 );
        feature.setGeometry(point);
        feature.setId("2423534545");
        feature.setProperties("{\"name\": \"mark\", \"colour\": \"red\"}");

        FeatureDto feature2 = new FeatureDto();
        PointDto point2 = new PointDto(79.86843824386597, 6.963606397102782);
        feature2.setGeometry(point2);
        feature2.setId("2423534545");
        feature2.setProperties("{\"name\": \"mark\", \"colour\": \"red\"}");

        FeatureCollectionDto featureCollection = new FeatureCollectionDto();
        featureCollection.setFeatures(Arrays.asList(feature,feature2));

        // String featureGeoJSON = FeatureBuilder.getInstance().toGeoJSON(feature);
        return FeatureCollectionBuilder.getInstance().toGeoJSON(featureCollection);
    }*/

    @RequestMapping(value = "/by-filters", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public ResponseEntity<List<ProduceDto>> getProduceByFiltersNew(@RequestParam String sw_lat,
                                                @RequestParam String ne_lat,
                                                @RequestParam String sw_lng,
                                                @RequestParam String ne_lng,
                                                                   @RequestParam boolean include_users,@RequestParam String category,@RequestParam String status, HttpServletRequest request) {

        List<ProduceDto > produceDtoList;
        if(include_users == true){
            produceDtoList = produceService.getFilteredProduces(sw_lat,  ne_lat,  sw_lng,  ne_lng,category,status,"");
        }
        else{
            Principal principal = request.getUserPrincipal();
            String username = principal.getName();
            produceDtoList = produceService.getFilteredProduces(sw_lat,  ne_lat,  sw_lng,  ne_lng,category,status,username);
        }
        return status(HttpStatus.OK).body(produceDtoList);

    }
/*
    @RequestMapping(value = "/geoJsonNew", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getProduceGeoJsonByFiltersNew(@RequestParam String sw_lat,
                                                @RequestParam String ne_lat,
                                                @RequestParam String sw_lng,
                                                @RequestParam String ne_lng,
                                                @RequestParam String category,
                                                @RequestParam String status,
                                                @RequestParam boolean include_users,
                                                HttpServletRequest request) {



        FeatureCollectionDto featureCollection = new FeatureCollectionDto();
        //creating a list of features
        List<FeatureDto> dtoList = new ArrayList<>();
        List<ProduceDto > produceDtoList;

        if(include_users == true){
            produceDtoList = produceService.getFilteredGeoJsonProduces(sw_lat,  ne_lat,  sw_lng,  ne_lng,category,status);
        }
        else{
            Principal principal = request.getUserPrincipal();
            String username = principal.getName();
            produceDtoList = produceService.getFilteredGeoJsonProducesWithoutUsers(sw_lat,  ne_lat,  sw_lng,  ne_lng,status,category,username);
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
                            "\"address\": \""+element.getAddress()+"\", "+
                            "\"grower\": \""+element.getUserName()+"\", "+
                            "\"filename\": \""+element.getFilename()+"\", "+
                            "\"publishStatus\": \""+element.getPublishStatus()+"\" "+"}");

            dtoList.add(feature);
        }
        featureCollection.setFeatures(dtoList);

       // String featureCollectionGeoJSON = FeatureCollectionBuilder.getInstance().toGeoJSON(featureCollection);

        return FeatureCollectionBuilder.getInstance().toGeoJSON(featureCollection);
    }*/



    String generateUniqueFileName() {
        String filename = "";
        long millis = System.currentTimeMillis();
        String datetime = new Date().toString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        String rndchars = RandomStringUtils.randomAlphanumeric(16);
        filename = rndchars + "_" + datetime + "_" + millis;
        return filename;
    }

}
