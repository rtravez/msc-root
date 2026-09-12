package com.rtravez.msc.repository;

import static com.rtravez.msc.entity.QPersonEntity.personEntity;
import static com.rtravez.msc.entity.QUserEntity.userEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPQLQuery;
import com.rtravez.msc.entity.UserEntity;
import com.rtravez.msc.exception.ExceptionManager;

import jakarta.persistence.EntityManager;

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
        return Optional.ofNullable(queryFactory.selectFrom(userEntity).innerJoin(userEntity.person, personEntity)
                .fetchJoin().where(userEntity.username.eq(username).and(userEntity.status.isTrue())).fetchFirst());
    }

    @Override
    public Optional<UserEntity> findUserByPersonIdentification(String identification) throws ExceptionManager {
        return Optional.ofNullable(queryFactory.selectFrom(userEntity)
            .innerJoin(userEntity.person, personEntity)
            .fetchJoin()
            .where(userEntity.person.identification.eq(identification)
                .and(userEntity.status.isTrue()))
            .fetchFirst());
    }

    @Override
    public Page<UserEntity> findAllByStatusTrue(Pageable pageable) throws ExceptionManager {
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

        return new PageImpl<>(Objects.requireNonNull(content), pageable, total == null ? 0 : total);
    }
}
