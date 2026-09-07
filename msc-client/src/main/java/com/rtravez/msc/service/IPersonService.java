package com.rtravez.msc.service;

import java.util.List;
import java.util.Optional;

import com.rtravez.msc.dto.request.PersonRequest;
import com.rtravez.msc.dto.response.PersonResponse;
import com.rtravez.msc.exception.ExceptionManager;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IPersonService {

    PersonResponse save(PersonRequest request) throws ExceptionManager;

	PersonResponse update(PersonRequest request) throws ExceptionManager;

	Optional<PersonResponse> findById(Long id) throws ExceptionManager;

	List<PersonResponse> findAll() throws ExceptionManager;

	void deleteById(Long id) throws ExceptionManager;

    /**
     * Find person by identification
     *
     * @param identification
     * @return
     * @throws ExceptionManager
     */
    Boolean exist(String identification) throws ExceptionManager;

    
}
