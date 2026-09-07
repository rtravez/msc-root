package com.rtravez.msc.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface UserRepository extends BaseRepository<UserEntity, Long> {

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
    Optional<UserEntity> findUserByIdentification(String identification) throws ExceptionManager;

    Page<UserEntity> findAllByStatusTrue(Pageable pageable) throws ExceptionManager;
}
