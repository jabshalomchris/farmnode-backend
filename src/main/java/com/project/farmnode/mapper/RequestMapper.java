package com.project.farmnode.mapper;

import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.dto.ProduceRequest.RequestDto;
import com.project.farmnode.dto.ProduceRequest.RequestItemsDto;
import com.project.farmnode.dto.ProduceRequest.RequestResponseDto;
import com.project.farmnode.model.Comment;
import com.project.farmnode.model.Request;
import com.project.farmnode.model.RequestItem;
import com.project.farmnode.repository.ProduceCommentRepo;
import com.project.farmnode.repository.RequestRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "growerId", expression = "java(request.getGrowerId().getUserId())")
    @Mapping(target = "buyerId", expression = "java(request.getBuyerId().getUserId())")
    RequestResponseDto mapToDto(Request request);

}




