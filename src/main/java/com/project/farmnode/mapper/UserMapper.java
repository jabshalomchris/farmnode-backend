package com.project.farmnode.mapper;

import com.project.farmnode.dto.UserDto;
import com.project.farmnode.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
   /* @Mapping(target = "commentId", ignore = true) //auto generated
    User map(UserDto userDto);*/

    UserDto mapToDto(User user);
}
