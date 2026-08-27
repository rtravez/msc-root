package com.rtravez.msc.service;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;
import com.rtravez.msc.repository.IUserRepository;
import com.rtravez.msc.web.ClientIpProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class UserService extends GenericService<UserEntity, Long, IUserRepository> implements IUserService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PersonService personService;
    @Autowired
    private ClientIpProvider clientIpProvider;

    protected UserService(IUserRepository repository) {
        super(repository);
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public UserResponse processSaveUser(UserRequest request) throws ExceptionManager {
        try {
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
            personService.save(person);

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
                    .password(user.getPassword())
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .status(user.getStatus()).build();
        } catch (Exception e) {
            log.error("processSaveUser: {0}", e);
            throw new ExceptionManager.GettingException("Error al guardar el registro");
        }
    }

    @Override
    @Transactional
    public UserResponse processUpdateUser(UserRequest request) throws ExceptionManager {
        try {
            Optional<UserEntity> user = repository.findUserByIdentification(request);

            return user.map(value -> this.updateUser(value, request)).orElse(null);
        } catch (Exception e) {
            log.error("processUpdateUser: {0}", e);
            throw new ExceptionManager.GettingException("Error al actualizar el registro");
        }
    }

    @Override
    public List<UserResponse> findUserAll() throws ExceptionManager {
        try {
            List<UserResponse> userResponses = new ArrayList<>();
            List<UserEntity> users = repository.findAll();
            users.forEach(it -> userResponses.add(UserResponse.builder()
                    .name(it.getPerson().getName())
                    .lastname(it.getPerson().getLastname())
                    .address(it.getPerson().getAddress())
                    .telephone(it.getPerson().getTelephone())
                    .identification(it.getPerson().getIdentification())
                    .password(it.getPassword())
                    .status(it.getStatus())
                    .username(it.getUsername())
                    .userId(it.getUserId())
                    .build()));
            return userResponses;
        } catch (Exception e) {
            log.error("findUserAll: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }

    @Override
    @Transactional
    public Long deleteUserById(Long id) throws ExceptionManager {
        try {
            Optional<UserEntity> user = repository.findById(id);

            if (user.isPresent()) {
                repository.deleteById(user.get().getUserId());
                personService.deleteById(user.get().getPerson().getPersonId());
                return 1L;
            }
            return 0L;
        } catch (ExceptionManager e) {
            log.error("deleteUserById: {0}", e);
            throw new ExceptionManager.DeletingException("Error al eliminar el registro");
        }
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
        personService.update(person);

        return UserResponse.builder()
                .name(person.getName())
                .lastname(person.getLastname())
                .address(person.getAddress())
                .telephone(person.getTelephone())
                .identification(person.getIdentification())
                .password(user.getPassword())
                .status(user.getStatus())
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();

    }

    @Override
    public UserResponse findUserByIdentification(UserRequest request) throws ExceptionManager {
        try {
            Optional<UserEntity> user = repository.findUserByIdentification(request);

            return user.map(userEntity -> UserResponse.builder()
                    .name(userEntity.getPerson().getName())
                    .lastname(userEntity.getPerson().getLastname())
                    .address(userEntity.getPerson().getAddress())
                    .telephone(userEntity.getPerson().getTelephone())
                    .password(userEntity.getPassword())
                    .status(userEntity.getStatus())
                    .userId(userEntity.getUserId())
                    .username(userEntity.getUsername())
                    .identification(userEntity.getPerson().getIdentification())
                    .build()).orElse(null);
        } catch (Exception e) {
            log.error("findUserByIdentification: {0}", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
