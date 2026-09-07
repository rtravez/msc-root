package com.rtravez.msc.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.rtravez.msc.entity.QUserEntity.userEntity;
import static com.rtravez.msc.entity.QPersonEntity.personEntity;

@Slf4j
@Repository
public class UserRepository extends SimpleJpaRepository<UserEntity, Long> implements IUserRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * Constructor
     */
    public UserRepository(EntityManager em) {
        super(UserEntity.class, em);
        this.queryFactory = new JPAQueryFactory(em);
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
                            .and(userEntity.status.isTrue()))
                    .fetchFirst());
        } catch (Exception e) {
            log.error("findUserByIdentification: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
