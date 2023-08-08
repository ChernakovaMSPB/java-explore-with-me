package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ValidationException;
import ru.practicum.main.mappers.UserMapper;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.UserService;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        Collection<User> users = null;
        if (ids == null || ids.isEmpty()) {
            users = repository.findAllBy(pageable);
        } else {
            users = repository.findAllByIdIsIn(ids, pageable);
        }
        return mapper.toUserDto(users);
    }

    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        checkUserName(userDto.getName());
        User user = mapper.toUser(userDto);
        User registeredUser = repository.save(user);
        return mapper.toUserDto(registeredUser);
    }

    @Transactional(readOnly = true)
    public void checkUserName(String name) {
        if (repository.findUserByName(name) != null) {
            throw new ValidationException("User name already exists.");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        repository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " is not found."));
        repository.deleteById(userId);
    }
}
