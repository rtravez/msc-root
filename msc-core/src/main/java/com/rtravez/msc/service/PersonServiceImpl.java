package com.rtravez.msc.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rtravez.msc.dto.request.PersonRequest;
import com.rtravez.msc.dto.response.PersonResponse;
import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.exception.ExceptionManager;
import com.rtravez.msc.mapper.PersonMapper;
import com.rtravez.msc.repository.PersonRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepositoryImpl personRepository;
    private final PersonMapper personMapper;

    @Override
    public Boolean exist(String identification) throws ExceptionManager {
        try {
            return personRepository.exist(identification);
        } catch (ExceptionManager e) {
            log.error("exist: {0}", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    public PersonResponse save(PersonRequest request) throws ExceptionManager {
        PersonEntity person = personMapper.toEntity(request);
        person.setStatus(request.getStatus() == null ? Boolean.TRUE : request.getStatus());
        return personMapper.toResponse(personRepository.save(person));
    }

    @Override
    public PersonResponse update(PersonRequest request) throws ExceptionManager {
        return personRepository.findById(Objects.requireNonNull(request.getPersonId()))
                .map(person -> updatePerson(person, request))
                .map(personRepository::save)
                .map(personMapper::toResponse)
                .orElseThrow(() -> new ExceptionManager.NotFoundException("La persona no existe"));
    }

    @Override
    public Optional<PersonResponse> findById(Long id) throws ExceptionManager {
        return personRepository.findById(Objects.requireNonNull(id))
                .filter(person -> Boolean.TRUE.equals(person.getStatus()))
                .map(personMapper::toResponse);
    }

    @Override
    public List<PersonResponse> findAll() throws ExceptionManager {
        return personRepository.findAll().stream()
                .filter(person -> Boolean.TRUE.equals(person.getStatus()))
                .map(personMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteById(Long id) throws ExceptionManager {
        personRepository.findById(Objects.requireNonNull(id))
                .ifPresent(person -> personRepository.deleteById(
                        Objects.requireNonNull(person.getPersonId())));
    }

    private PersonEntity updatePerson(PersonEntity person, PersonRequest request) {
        person.setIdentification(request.getIdentification());
        person.setName(request.getName());
        person.setLastname(request.getLastname());
        person.setAddress(request.getAddress());
        person.setTelephone(request.getTelephone());
        person.setGender(request.getGender());
        person.setAge(request.getAge());
        person.setStatus(request.getStatus() == null ? person.getStatus() : request.getStatus());
        person.setLastModifiedHost(request.getLastModifiedHost());
        return person;
    }

}
