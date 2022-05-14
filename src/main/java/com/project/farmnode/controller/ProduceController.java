package com.project.farmnode.controller;

import com.project.farmnode.common.ApiResponse;
import com.project.farmnode.dto.GeoJsonBuilder.feature.FeatureCollectionBuilder;
import com.project.farmnode.dto.GeoJsonModel.feature.FeatureCollectionDto;
import com.project.farmnode.dto.GeoJsonModel.feature.FeatureDto;
import com.project.farmnode.dto.GeoJsonModel.geometry.PointDto;
import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.dto.ProduceFilterDto;
import com.project.farmnode.service.ProduceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
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
        return new ResponseEntity<>(new ApiResponse(true, "Produce has been added"), HttpStatus.CREATED);
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
    @GetMapping("geoJson")
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
    }

    @RequestMapping(value = "geoJsonNew", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getProduceGeoJsonByFiltersNew(@RequestParam String sw_lat,
                                                @RequestParam String ne_lat,
                                                @RequestParam String sw_lng,
                                                @RequestParam String ne_lng) {

        FeatureCollectionDto featureCollection = new FeatureCollectionDto();
        //creating a list of features
        List<FeatureDto> dtoList = new ArrayList<>();

        List<ProduceDto > produceDtoList = produceService.getFilteredGeoJsonProduces(sw_lat,  ne_lat,  sw_lng,  ne_lng);
        for (ProduceDto element : produceDtoList) {
            FeatureDto feature = new FeatureDto();
            PointDto point = new PointDto(element.getLongitude(), element.getLatitude() );
            feature.setGeometry(point);
            feature.setId(element.getProduceId().toString());
            feature.setProperties
                    ("{\"Name\": \""+element.getProduceName()+"\", "+
                            "\"Description\": \""+element.getDescription()+"\", "+
                            "\"ProduceStatus\": \""+element.getProduceStatus()+"\", "+
                            "\"price\": \""+element.getPrice()+"\", "+
                            "\"category\": \""+element.getCategory()+"\", "+
                            "\"address\": \""+element.getAddress()+"\", "+
                            "\"grower\": \""+element.getUserName()+"\", "+
                            "\"PublishStatus\": \""+element.getPublishStatus()+"\" "+"}");

            dtoList.add(feature);
        }
        featureCollection.setFeatures(dtoList);

       // String featureCollectionGeoJSON = FeatureCollectionBuilder.getInstance().toGeoJSON(featureCollection);

        return FeatureCollectionBuilder.getInstance().toGeoJSON(featureCollection);
    }

}
