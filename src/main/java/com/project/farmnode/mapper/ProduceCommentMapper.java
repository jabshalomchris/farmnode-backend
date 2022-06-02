package com.project.farmnode.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.project.farmnode.dto.ProduceCommentsDto;
import com.project.farmnode.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Mapper(componentModel = "spring")
public abstract class ProduceCommentMapper {
    @Mapping(target = "produceCommentId", ignore = true) //auto generated
    @Mapping(target = "text", source = "produceCommentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "produce", source = "produce")
    @Mapping(target = "user", source = "user")
    public abstract ProduceComment map(ProduceCommentsDto produceCommentsDto, Produce produce, User user);

    @Mapping(target = "produceId", expression = "java(produceComment.getProduce().getProduceId())")
    @Mapping(target = "userName", expression = "java(produceComment.getUser().getName())")
    @Mapping(target = "filename", expression = "java(produceComment.getUser().getFilename())")
    @Mapping(target = "createdDate", expression = "java(getDuration(produceComment))")
    public abstract ProduceCommentsDto mapToDto(ProduceComment produceComment);

    String getDuration(ProduceComment produceComment) {

        Timestamp timestamp = Timestamp.from(produceComment.getCreatedDate());
        Date date = new Date(timestamp.getTime());
        return date.toString();
    }
}
