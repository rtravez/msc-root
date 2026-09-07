package com.rtravez.msc.repository;

import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.exception.ExceptionManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import static com.rtravez.msc.entity.QUserEntity.userEntity;
import static com.rtravez.msc.entity.QPersonEntity.personEntity;

@Slf4j
@Repository
public class PersonRepositoryImpl extends BaseRepositoryImpl<PersonEntity, Long> implements PersonRepository {
    public PersonRepositoryImpl(EntityManager em) {
        super(PersonEntity.class, em);
    }

    @Override
    public Boolean exist(String identification) throws ExceptionManager {
        try {
            BooleanBuilder where = new BooleanBuilder();
            where.and(personEntity.identification.eq(identification));
            where.and(personEntity.status.isTrue());

            JPQLQuery<String> query = queryFactory.selectFrom(personEntity).select(personEntity.identification)
                    .innerJoin(personEntity.users, userEntity)
                    .where(where);
            return StringUtils.isNotBlank(query.fetchFirst());
        } catch (ExceptionManager e) {
            log.error("exist: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }

    }
}
