package com.sofka.msc.repository;

import com.sofka.msc.dto.request.CustomerRequest;
import com.sofka.msc.entity.CustomerEntity;
import com.sofka.msc.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.sofka.msc.entity.QCustomerEntity.customerEntity;
import static com.sofka.msc.entity.QPersonEntity.personEntity;

@Slf4j
@Repository
public class CustomerRepository extends GenericRepository<CustomerEntity, Long> implements ICustomerRepository {

    /**
     * Constructor
     */
    public CustomerRepository() {
        super(CustomerEntity.class);
    }

    @Override
    public Optional<CustomerEntity> findCustomerByUsername(String username) throws ExceptionManager {
        try {
            return Optional.ofNullable(queryFactory.selectFrom(customerEntity).innerJoin(customerEntity.person, personEntity)
                    .fetchJoin().where(customerEntity.username.eq(username).and(customerEntity.status.isTrue())).fetchFirst());
        } catch (Exception e) {
            log.error("findCustomerByUsername: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    public Optional<CustomerEntity> findCustomerByIdentification(CustomerRequest request) throws ExceptionManager {
        try {
            return Optional.ofNullable(queryFactory.selectFrom(customerEntity)
                    .innerJoin(customerEntity.person, personEntity)
                    .fetchJoin()
                    .where(customerEntity.person.identification.eq(request.getIdentification())
                            .and(customerEntity.status.isTrue())).fetchFirst());
        } catch (Exception e) {
            log.error("findCustomerByIdentification: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
