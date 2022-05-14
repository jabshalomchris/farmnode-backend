package com.project.farmnode.dto.GeoJsonBuilder.geometry;

import com.project.farmnode.dto.GeoJsonBuilder.BuilderConstants;
import com.project.farmnode.dto.GeoJsonModel.geometry.GeometryDto;
import com.project.farmnode.dto.GeoJsonModel.geometry.PointDto;

import java.util.HashMap;
import java.util.Map;

public class CommonGeometryBuilder {
    private static Map<Class<? extends GeometryDto>, GeometryBuilder<?>> builders = new HashMap<>();

    static {
        builders.put(PointDto.class, PointBuilder.getInstance());
        /*builders.put(LineStringDto.class, LineStringBuilder.getInstance());
        builders.put(PolygonDto.class, PolygonBuilder.getInstance());
        builders.put(MultiPointDto.class, MultiPointBuilder.getInstance());
        builders.put(MultiLineStringDto.class, MultiLineStringBuilder.getInstance());
        builders.put(MultiPolygonDto.class, MultiPolygonBuilder.getInstance());
        builders.put(GeometryCollectionDto.class, GeometryCollectionBuilder.getInstance());*/
    }

    private CommonGeometryBuilder() {
    }

    /**
     * Get suitable GeometryBuilder
     *
     * @param geometryDto
     *            An instance of GeometryDto
     * @return GeometryBuilder
     */
    public static GeometryBuilder getBuilder(GeometryDto geometryDto) {
        if (geometryDto == null) {
            return null;
        }
        return builders.get(geometryDto.getClass());
    }

    /**
     * Find suitable builder and return build result;
     *
     * @param geometryDto
     * @return builder
     */
    @SuppressWarnings("unchecked")
    public static String toGeometryGeoJSON(GeometryDto geometryDto) {
        GeometryBuilder builder = getBuilder(geometryDto);
        if (builder != null) {
            return builder.toGeoJSON(geometryDto);
        }
        return BuilderConstants.NULL_VALUE;
    }
}
