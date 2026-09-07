package com.rtravez.msc.repository;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.querydsl.jpa.JPQLQuery;

import static com.rtravez.msc.entity.QUserEntity.userEntity;
import static com.rtravez.msc.entity.QPersonEntity.personEntity;

@Slf4j
@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<UserEntity, Long> implements UserRepository {
    /**
     * Constructor
     */
    public UserRepositoryImpl(EntityManager em) {
        super(UserEntity.class, em);
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
    public Optional<UserEntity> findUserByIdentification(String identification) throws ExceptionManager {
        try {
            return Optional.ofNullable(queryFactory.selectFrom(userEntity)
                    .innerJoin(userEntity.person, personEntity)
                    .fetchJoin()
                    .where(userEntity.person.identification.eq(identification)
                            .and(userEntity.status.isTrue()))
                    .fetchFirst());
        } catch (Exception e) {
            log.error("findUserByIdentification: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    public Page<UserEntity> findAllByStatusTrue(Pageable pageable) throws ExceptionManager {
        try {
            JPQLQuery<UserEntity> contentQuery = queryFactory.selectFrom(userEntity)
                    .innerJoin(userEntity.person, personEntity)
                    .fetchJoin()
                    .where(userEntity.status.isTrue())
                    .orderBy(userEntity.userId.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<UserEntity> content = contentQuery.fetch();
            Long total = queryFactory.select(userEntity.userId.count())
                    .from(userEntity)
                    .where(userEntity.status.isTrue())
                    .fetchOne();

            return new PageImpl<>(content, pageable, total == null ? 0 : total);
        } catch (Exception e) {
            log.error("findAllByStatusTrue: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }
}
