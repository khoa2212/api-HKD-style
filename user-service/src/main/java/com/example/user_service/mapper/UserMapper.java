package com.example.user_service.mapper;

import com.example.user_service.dto.AddUserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserResponseDTO toUserResponseDTO(User user);
    public User toUser(AddUserRequestDTO userDTO);
}
