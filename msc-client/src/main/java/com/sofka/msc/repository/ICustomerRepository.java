package com.sofka.msc.repository;

import com.sofka.msc.dto.request.CustomerRequest;
import com.sofka.msc.entity.CustomerEntity;
import com.sofka.msc.exception.ExceptionManager;

import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface ICustomerRepository extends IGenericRepository<CustomerEntity, Long> {

    /**
     * Find customer by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<CustomerEntity> findCustomerByUsername(String username) throws ExceptionManager;

    /**
     * Find customer by identification
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    Optional<CustomerEntity> findCustomerByIdentification(CustomerRequest request) throws ExceptionManager;
}
