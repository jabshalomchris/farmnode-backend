package com.project.farmnode.mapper;

import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.dto.UserDto;
import com.project.farmnode.model.Comment;
import com.project.farmnode.model.Post;
import com.project.farmnode.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
   /* @Mapping(target = "commentId", ignore = true) //auto generated
    User map(UserDto userDto);*/

    UserDto mapToDto(User user);
}
