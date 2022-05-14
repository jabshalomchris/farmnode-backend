package com.project.farmnode.dto.GeoJsonBuilder.feature;

import com.project.farmnode.dto.GeoJsonBuilder.BuilderConstants;
import com.project.farmnode.dto.GeoJsonBuilder.GeoJSONBuilder;
import com.project.farmnode.dto.GeoJsonBuilder.geometry.CommonGeometryBuilder;
import com.project.farmnode.dto.GeoJsonModel.GeoJSONObjectTypeEnum;
import com.project.farmnode.dto.GeoJsonModel.feature.FeatureDto;

public class FeatureBuilder extends GeoJSONBuilder<FeatureDto> {
    private static final FeatureBuilder INSTANCE = new FeatureBuilder();

    private FeatureBuilder() {
    }

    public static FeatureBuilder getInstance() {
        return INSTANCE;
    }

    /**
     * Build Feature GeoJSON using FeatureDto object
     *
     * @param featureDto
     * @return Feature GeoJSON
     */
    @Override
    public String toGeoJSON(FeatureDto featureDto) {
        if (featureDto == null) {
            return BuilderConstants.NULL_VALUE;
        }

        StringBuilder builder = initializeBuilder();
        buildTypePart(builder, GeoJSONObjectTypeEnum.Feature);

        builder.append(BuilderConstants.GEOMETRY_SPACE);
        if (featureDto.getGeometry() == null) {
            builder.append(BuilderConstants.NULL_VALUE);
        } else {
            builder.append(CommonGeometryBuilder.toGeometryGeoJSON(featureDto.getGeometry()));
        }

        builder.append(BuilderConstants.COMMA_NEWLINE);
        builder.append(BuilderConstants.PROPERTIES_SPACE);

        builder.append(featureDto.getProperties());

        if (featureDto.getId() != null) {
            builder.append(BuilderConstants.COMMA_NEWLINE);
            builder.append(BuilderConstants.ID_SPACE);
            builder.append(featureDto.getId());
        }

        buildBbox(builder, featureDto.getBbox());
        endBuilder(builder);

        return builder.toString();
    }

}
