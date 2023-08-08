package ru.practicum.main.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.model.User;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDto(Collection<User> user);
}

