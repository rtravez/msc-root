package com.rtravez.msc.service;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;
import com.rtravez.msc.mapper.UserMapper;
import com.rtravez.msc.mapper.UserRequestToPersonMapper;
import com.rtravez.msc.repository.IPersonRepository;
import com.rtravez.msc.repository.IUserRepository;
import com.rtravez.msc.web.ClientIpProvider;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
public class UserService extends GenericService<UserEntity, Long, IUserRepository> implements IUserService {
    
    private final IPersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ClientIpProvider clientIpProvider;
    private final UserMapper userMapper;
    private final UserRequestToPersonMapper userRequestToPersonMapper;

    protected UserService(IUserRepository repository,
            IPersonRepository personRepository,
            BCryptPasswordEncoder passwordEncoder,
            ClientIpProvider clientIpProvider,
            UserMapper userMapper,
            UserRequestToPersonMapper userRequestToPersonMapper) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.clientIpProvider = clientIpProvider;
        this.userMapper = userMapper;
        this.userRequestToPersonMapper = userRequestToPersonMapper;
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public UserResponse processSaveUser(UserRequest request) throws ExceptionManager {
        // Map request to PersonEntity
        PersonEntity person = userRequestToPersonMapper.userRequestToPersonEntity(request);
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
        super.save(user);

        // Map UserEntity to UserResponse
        return userMapper.userEntityToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse processUpdateUser(UserRequest request) throws ExceptionManager {
        Optional<UserEntity> user = repository.findUserByIdentification(request);

        return user.map(value -> this.updateUser(value, request))
                .orElseThrow(() -> new ExceptionManager.NotFoundException("El usuario no existe"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findUserAll() throws ExceptionManager {
        return repository.findAll().stream()
                .filter(it -> Boolean.TRUE.equals(it.getStatus()))
                .map(userMapper::userEntityToUserResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long deleteUserById(Long id) throws ExceptionManager {
        Optional<UserEntity> user = repository.findById(id);

        if (user.isPresent()) {
            repository.deleteById(user.get().getUserId());
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
        super.update(user);

        PersonEntity person = getPerson(user, request);
        personRepository.update(person);

        return userMapper.userEntityToUserResponse(user);

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
        return repository.findUserByIdentification(request)
                .map(userMapper::userEntityToUserResponse)
                .orElseThrow(() -> new ExceptionManager.NotFoundException("El usuario no existe"));
    }
}
