package com.rtravez.msc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rtravez.msc.dto.request.PersonRequest;
import com.rtravez.msc.dto.response.PersonResponse;
import com.rtravez.msc.exception.ExceptionManager;
import com.rtravez.msc.mapper.PersonMapper;
import com.rtravez.msc.repository.IPersonRepository;

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
public class PersonService implements IPersonService {

    private final IPersonRepository personRepository;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public PersonResponse update(PersonRequest request) throws ExceptionManager {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Optional<PersonResponse> findById(Long id) throws ExceptionManager {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<PersonResponse> findAll() throws ExceptionManager {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public void deleteById(Long id) throws ExceptionManager {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
