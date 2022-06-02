package com.project.farmnode.mapper;

import com.project.farmnode.dto.FellowUserDto;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FellowUserMapper {
    @Autowired
    private ProduceRepo produceRepo;

    @Mapping(target = "name", expression = "java(user.getName())")
    @Mapping(target = "produceCount", expression = "java(produceCount(user))")
    @Mapping(target = "filename", expression = "java(user.getFilename())")
    public abstract FellowUserDto mapToDto(User user, String friendship);

    Integer produceCount(User user) {
        return produceRepo.findByUser(user).size();
    }
}
