package com.project.farmnode.dto.GeoJsonModel;

import com.project.farmnode.dto.GeoJsonModel.feature.FeatureCollectionDto;
import com.project.farmnode.dto.GeoJsonModel.feature.FeatureDto;
import com.project.farmnode.dto.GeoJsonModel.geometry.PointDto;

public enum GeoJSONObjectTypeEnum {
    /*Point(PointDto.class), MultiPoint(MultiPointDto.class), LineString(LineStringDto.class), MultiLineString(
            MultiLineStringDto.class), Polygon(PolygonDto.class), MultiPolygon(
            MultiPolygonDto.class), GeometryCollection(GeometryCollectionDto.class), Feature(
            FeatureDto.class), FeatureCollection(FeatureCollectionDto.class);*/
    Point(PointDto.class), Feature(
            FeatureDto.class), FeatureCollection(FeatureCollectionDto.class);

    private final Class dtoClass;

    private GeoJSONObjectTypeEnum(Class dtoClass) {
        this.dtoClass = dtoClass;
    }

    public Class getDtoClass() {
        return dtoClass;
    }

}
