package com.project.farmnode.mapper;

import com.project.farmnode.dto.ProduceCommentsDto;
import com.project.farmnode.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProduceCommentMapper {
    @Mapping(target = "produceCommentId", ignore = true) //auto generated
    @Mapping(target = "text", source = "produceCommentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "produce", source = "produce")
    @Mapping(target = "user", source = "user")
    ProduceComment map(ProduceCommentsDto produceCommentsDto, Produce produce, User user);

    @Mapping(target = "produceId", expression = "java(produceComment.getProduce().getProduceId())")
    @Mapping(target = "userName", expression = "java(produceComment.getUser().getUsername())")
    ProduceCommentsDto mapToDto(ProduceComment produceComment);
}
