package com.rtravez.msc.service;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface UserService {

    /**
     * Find user by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<UserResponse> findUserByUsername(String username) throws ExceptionManager;

    /**
     * Save user
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    UserResponse save(UserRequest request) throws ExceptionManager;

    /**
     * Update user
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    UserResponse update(Long id, UserRequest request) throws ExceptionManager;

    /**
     * Find user all
     *
     * @return
     * @throws ExceptionManager
     */
    Page<UserResponse> findUserAll(Pageable pageable) throws ExceptionManager;

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
    UserResponse findUserById(Long id) throws ExceptionManager;

    UserResponse findUserByIdentification(String identification) throws ExceptionManager;
}
