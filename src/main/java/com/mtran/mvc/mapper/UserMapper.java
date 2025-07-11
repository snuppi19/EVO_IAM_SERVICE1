package com.mtran.mvc.mapper;

import com.mtran.mvc.dto.UserDTO;
import com.mtran.mvc.entity.User;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUserEntity(UserDTO userDTO);
}
