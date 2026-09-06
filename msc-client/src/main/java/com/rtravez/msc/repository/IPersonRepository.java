package com.rtravez.msc.repository;

import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.exception.ExceptionManager;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@NoRepositoryBean
public interface IPersonRepository extends IGenericRepository<PersonEntity, Long> {

    /**
     * Find person by identification
     *
     * @param identification
     * @return
     * @throws ExceptionManager
     */
    Boolean exist(String identification) throws ExceptionManager;
}
