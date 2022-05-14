package com.project.farmnode.dto.GeoJsonModel.feature;

import com.project.farmnode.dto.GeoJsonModel.GeoJSONObjectDto;
import com.project.farmnode.dto.GeoJsonModel.GeoJSONObjectTypeEnum;

import java.util.List;

public class FeatureCollectionDto extends GeoJSONObjectDto {
    private static final long serialVersionUID = 1L;

    private List<FeatureDto> features;

    @Override
    public GeoJSONObjectTypeEnum getGeoJSONObjectType() {
        return GeoJSONObjectTypeEnum.FeatureCollection;
    }

    public List<FeatureDto> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureDto> features) {
        this.features = features;
    }

}
