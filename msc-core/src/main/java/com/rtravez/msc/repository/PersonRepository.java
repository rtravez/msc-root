package com.sofka.msc.repository;

import com.sofka.msc.entity.PersonEntity;
import com.sofka.msc.exception.ExceptionManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import static com.sofka.msc.entity.QCustomerEntity.customerEntity;
import static com.sofka.msc.entity.QPersonEntity.personEntity;


@Slf4j
@Repository
public class PersonRepository extends GenericRepository<PersonEntity, Long> implements IPersonRepository {

    public PersonRepository() {
        super(PersonEntity.class);
    }

    @Override
    public Boolean exist(String identification) throws ExceptionManager {
        try {
            BooleanBuilder where = new BooleanBuilder();
            where.and(personEntity.identification.eq(identification));
            where.and(personEntity.status.isTrue());

            JPQLQuery<String> query = queryFactory.selectFrom(personEntity).select(personEntity.identification)
                    .innerJoin(personEntity.customers, customerEntity)
                    .where(where);
            return StringUtils.isNotBlank(query.fetchFirst());
        } catch (ExceptionManager e) {
            log.error("exist: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }

    }
}
