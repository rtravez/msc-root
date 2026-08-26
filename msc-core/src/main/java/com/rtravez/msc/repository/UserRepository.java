package com.rtravez.msc.repository;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.rtravez.msc.entity.QUserEntity.userEntity;
import static com.rtravez.msc.entity.QPersonEntity.personEntity;

@Slf4j
@Repository
public class UserRepository extends GenericRepository<UserEntity, Long> implements IUserRepository {

    /**
     * Constructor
     */
    public UserRepository() {
        super(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) throws ExceptionManager {
        try {
            return Optional.ofNullable(queryFactory.selectFrom(userEntity).innerJoin(userEntity.person, personEntity)
                    .fetchJoin().where(userEntity.username.eq(username).and(userEntity.status.isTrue())).fetchFirst());
        } catch (Exception e) {
            log.error("findUserByUsername: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    public Optional<UserEntity> findUserByIdentification(UserRequest request) throws ExceptionManager {
        try {
            return Optional.ofNullable(queryFactory.selectFrom(userEntity)
                    .innerJoin(userEntity.person, personEntity)
                    .fetchJoin()
                    .where(userEntity.person.identification.eq(request.getIdentification())
                            .and(userEntity.status.isTrue())).fetchFirst());
        } catch (Exception e) {
            log.error("findUserByIdentification: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
