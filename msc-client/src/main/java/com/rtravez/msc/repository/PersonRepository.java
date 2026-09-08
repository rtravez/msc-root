package com.rtravez.msc.repository;

import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.exception.ExceptionManager;

/**
 * <b> Description de la class, interface or enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface PersonRepository extends BaseRepository<PersonEntity, Long> {

    /**
     * Find a person by identification
     *
     * @param identification
     * @return
     * @throws ExceptionManager
     */
    Boolean exist(String identification) throws ExceptionManager;
}
