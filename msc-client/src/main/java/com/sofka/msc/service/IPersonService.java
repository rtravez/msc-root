package com.sofka.msc.service;

import com.sofka.msc.entity.PersonEntity;
import com.sofka.msc.exception.ExceptionManager;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IPersonService extends IGenericService<PersonEntity, Long> {

    /**
     * Find person by identification
     *
     * @param identification
     * @return
     * @throws ExceptionManager
     */
    Boolean exist(String identification) throws ExceptionManager;
}
