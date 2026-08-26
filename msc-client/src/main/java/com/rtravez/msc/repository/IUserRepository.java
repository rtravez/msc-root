package com.rtravez.msc.repository;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;

import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IUserRepository extends IGenericRepository<UserEntity, Long> {

    /**
     * Find user by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<UserEntity> findUserByUsername(String username) throws ExceptionManager;

    /**
     * Find user by identification
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    Optional<UserEntity> findUserByIdentification(UserRequest request) throws ExceptionManager;
}
