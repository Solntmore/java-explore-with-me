package ru.practicum.ewmserv.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserv.user.dto.RequestUserDto;
import ru.practicum.ewmserv.user.dto.ResponseUserDto;
import ru.practicum.ewmserv.user.exceptions.UserNotFoundException;
import ru.practicum.ewmserv.user.mapper.UserMapper;
import ru.practicum.ewmserv.user.model.User;
import ru.practicum.ewmserv.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    public ResponseUserDto addUser(RequestUserDto requestUserDto) {
        User user = userRepository.save(
                userMapper.toEntity(requestUserDto));

        return userMapper.toDto(user);
    }

    public void deleteUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }
        userRepository.deleteById(userId);
    }

    public List<ResponseUserDto> getUsers(Integer[] ids, PageRequest pageRequest) {
        Collection<Long> idList = Arrays.stream(ids).map(Integer::longValue).collect(Collectors.toList());
        List<User> users = userRepository.getUsers(idList, pageRequest).getContent();

        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }
}
