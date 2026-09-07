package com.rtravez.msc.service;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;
import com.rtravez.msc.mapper.UserMapper;
import com.rtravez.msc.repository.IPersonRepository;
import com.rtravez.msc.repository.IUserRepository;
import com.rtravez.msc.web.ClientIpProvider;

import lombok.RequiredArgsConstructor;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IPersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ClientIpProvider clientIpProvider;
    private final UserMapper userMapper;

    @Override
    public Optional<UserResponse> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).map(userMapper::toResponse);
    }

    @Override
    @Transactional
    public UserResponse save(UserRequest request) throws ExceptionManager {
        // Map request to PersonEntity
        PersonEntity person = userMapper.toEntity(request);
        person.setStatus(request.getStatus());
        person.setCreatedHost(clientIpProvider.getCurrentIp());
        personRepository.save(person);

        // Create and save UserEntity
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .person(person)
                .build();
        user.setStatus(request.getStatus());
        user.setCreatedHost(clientIpProvider.getCurrentIp());
        userRepository.save(user);

        // Map UserEntity to UserResponse
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse update(UserRequest request) throws ExceptionManager {
        Optional<UserEntity> user = userRepository.findUserByIdentification(request);

        return user.map(value -> this.updateUser(value, request))
                .orElseThrow(() -> new ExceptionManager.NotFoundException("El usuario no existe"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findUserAll() throws ExceptionManager {
        return userRepository.findAll().stream()
                .filter(it -> Boolean.TRUE.equals(it.getStatus()))
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long deleteUserById(Long id) throws ExceptionManager {
        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(user.get().getUserId());
            personRepository.deleteById(user.get().getPerson().getPersonId());
            return 1L;
        }
        return 0L;
    }

    /**
     * Update user
     *
     * @param user
     * @param request
     * @return
     */
    private UserResponse updateUser(UserEntity user, UserRequest request) {
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPerson(user.getPerson());

        user.setStatus(request.getStatus());
        user.setLastModifiedHost(clientIpProvider.getCurrentIp());
        userRepository.save(user);

        PersonEntity person = getPerson(user, request);
        personRepository.save(person);

        return userMapper.toResponse(user);

    }

    private @NonNull PersonEntity getPerson(UserEntity user, UserRequest request) {
        PersonEntity person = user.getPerson();
        person.setName(request.getName());
        person.setLastname(request.getLastname());
        person.setIdentification(request.getIdentification());
        person.setAge(request.getAge());
        person.setAddress(request.getAddress());
        person.setTelephone(request.getTelephone());
        person.setGender(request.getGender());

        person.setStatus(request.getStatus());
        person.setLastModifiedHost(clientIpProvider.getCurrentIp());
        return person;
    }

    @Override
    public UserResponse findUserByIdentification(UserRequest request) throws ExceptionManager {
        return userRepository.findUserByIdentification(request)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ExceptionManager.NotFoundException("El usuario no existe"));
    }
}
