package com.project.farmnode.exception;

import com.project.farmnode.dto.GeoJsonModel.PositionDto;

public class InvalidPositionDtoException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InvalidPositionDtoException(PositionDto position) {
        super("Invalid position dto: " + position);
    }

}
