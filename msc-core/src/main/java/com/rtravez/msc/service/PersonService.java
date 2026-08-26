package com.sofka.msc.service;

import com.sofka.msc.entity.PersonEntity;
import com.sofka.msc.exception.ExceptionManager;
import com.sofka.msc.repository.IPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
public class PersonService extends GenericService<PersonEntity, Long, IPersonRepository> implements IPersonService {

    public PersonService(IPersonRepository repository) {
        super(repository);
    }

    @Override
    public Boolean exist(String identification) throws ExceptionManager {
        try {
            return repository.exist(identification);
        } catch (ExceptionManager e) {
            log.error("exist: {0}", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
