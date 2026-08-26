package com.rtravez.msc.service;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;

import java.util.List;
import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IUserService extends IGenericService<UserEntity, Long> {

    /**
     * Find user by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<UserEntity> findUserByUsername(String username) throws ExceptionManager;

    /**
     * Save user
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    UserResponse processSaveUser(UserRequest request) throws ExceptionManager;

    /**
     * Update user
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    UserResponse processUpdateUser(UserRequest request) throws ExceptionManager;

    /**
     * Find user all
     *
     * @return
     * @throws ExceptionManager
     */
    List<UserResponse> findUserAll() throws ExceptionManager;

    /**
     * Delete user by id
     *
     * @param id
     * @return
     * @throws ExceptionManager
     */
    Long deleteUserById(Long id) throws ExceptionManager;

    /**
     * Find user by identification
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    UserResponse findUserByIdentification(UserRequest request) throws ExceptionManager;
}
