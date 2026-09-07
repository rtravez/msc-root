package com.rtravez.msc.service;

import org.springframework.stereotype.Service;

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
public abstract class PersonService implements IPersonService {

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

}
