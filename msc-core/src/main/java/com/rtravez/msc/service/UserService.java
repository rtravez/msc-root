package com.rtravez.msc.service;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;
import com.rtravez.msc.repository.IPersonRepository;
import com.rtravez.msc.repository.IUserRepository;
import com.rtravez.msc.web.ClientIpProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    protected UserService(IUserRepository repository,
            IPersonRepository personRepository,
            BCryptPasswordEncoder passwordEncoder,
            ClientIpProvider clientIpProvider) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.clientIpProvider = clientIpProvider;
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public UserResponse processSaveUser(UserRequest request) throws ExceptionManager {
        PersonEntity person = PersonEntity.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .identification(request.getIdentification())
                .age(request.getAge())
                .address(request.getAddress())
                .telephone(request.getTelephone())
                .gender(request.getGender())
                .build();

        person.setStatus(request.getStatus());
        person.setCreatedHost(clientIpProvider.getCurrentIp());
        personRepository.save(person);

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .person(person)
                .build();

        user.setStatus(request.getStatus());
        user.setCreatedHost(clientIpProvider.getCurrentIp());
        super.save(user);

        return UserResponse.builder()
                .name(person.getName())
                .lastname(person.getLastname())
                .address(person.getAddress())
                .telephone(person.getTelephone())
                .identification(person.getIdentification())
                .userId(user.getUserId())
                .username(user.getUsername())
                .status(user.getStatus()).build();
    }

    @Override
    @Transactional
    public UserResponse processUpdateUser(UserRequest request) throws ExceptionManager {
        Optional<UserEntity> user = repository.findUserByIdentification(request);

        return user.map(value -> this.updateUser(value, request))
                .orElseThrow(() -> new ExceptionManager.NotFoundException("El usuario no existe"));
    }

    @Override
    public List<UserResponse> findUserAll() throws ExceptionManager {
        List<UserResponse> userResponses = new ArrayList<>();
        List<UserEntity> users = repository.findAll();
        users.forEach(it -> userResponses.add(UserResponse.builder()
                .name(it.getPerson().getName())
                .lastname(it.getPerson().getLastname())
                .address(it.getPerson().getAddress())
                .telephone(it.getPerson().getTelephone())
                .identification(it.getPerson().getIdentification())
                .status(it.getStatus())
                .username(it.getUsername())
                .userId(it.getUserId())
                .build()));
        return userResponses;
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
        personRepository.update(person);

        return UserResponse.builder()
                .name(person.getName())
                .lastname(person.getLastname())
                .address(person.getAddress())
                .telephone(person.getTelephone())
                .identification(person.getIdentification())
                .status(user.getStatus())
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();

    }

    @Override
    public UserResponse findUserByIdentification(UserRequest request) throws ExceptionManager {
        Optional<UserEntity> user = repository.findUserByIdentification(request);

        return user.map(userEntity -> UserResponse.builder()
                .name(userEntity.getPerson().getName())
                .lastname(userEntity.getPerson().getLastname())
                .address(userEntity.getPerson().getAddress())
                .telephone(userEntity.getPerson().getTelephone())
                .status(userEntity.getStatus())
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .identification(userEntity.getPerson().getIdentification())
                .build()).orElseThrow(() -> new ExceptionManager.NotFoundException("El usuario no existe"));
    }
}
