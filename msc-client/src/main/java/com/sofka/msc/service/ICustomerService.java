package com.sofka.msc.service;

import com.sofka.msc.dto.request.CustomerRequest;
import com.sofka.msc.dto.response.CustomerResponse;
import com.sofka.msc.entity.CustomerEntity;
import com.sofka.msc.exception.ExceptionManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface ICustomerService extends IGenericService<CustomerEntity, Long>, UserDetailsService {

    /**
     * Find customer by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<CustomerEntity> findCustomerByUsername(String username) throws ExceptionManager;

    /**
     * Save customer
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    CustomerResponse processSaveCustomer(CustomerRequest request) throws ExceptionManager;

    /**
     * Update customer
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    CustomerResponse processUpdateCustomer(CustomerRequest request) throws ExceptionManager;

    /**
     * Find customer all
     *
     * @return
     * @throws ExceptionManager
     */
    List<CustomerResponse> findCustomerAll() throws ExceptionManager;

    /**
     * Delete customer by id
     *
     * @param id
     * @return
     * @throws ExceptionManager
     */
    Long deleteCustomerById(Long id) throws ExceptionManager;

    /**
     * Find customer by identification
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    CustomerResponse findCustomerByIdentification(CustomerRequest request) throws ExceptionManager;
}
