package com.sofka.msc.service;

import com.sofka.msc.dto.request.CustomerRequest;
import com.sofka.msc.dto.response.CustomerResponse;
import com.sofka.msc.entity.CustomerEntity;
import com.sofka.msc.entity.PersonEntity;
import com.sofka.msc.exception.ExceptionManager;
import com.sofka.msc.repository.ICustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sofka.msc.common.Constants.CREATION_HOST;
import static com.sofka.msc.common.Constants.CREATION_USER;
import static com.sofka.msc.common.Constants.MODIFICATION_HOST;
import static com.sofka.msc.common.Constants.MODIFICATION_USER;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
public class CustomerService extends GenericService<CustomerEntity, Long, ICustomerRepository> implements ICustomerService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PersonService personService;

    protected CustomerService(ICustomerRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomerEntity> customer = this.findCustomerByUsername(username);

        if (customer.isEmpty()) {
            log.error("Error en el Login: no existe el usuario '{}' en el sistema!", username);
            throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema!");
        }

        List<SimpleGrantedAuthority> authorities = customer.get().getRoleCustomers().stream().map(role -> new SimpleGrantedAuthority(role.getRole().getName())).collect(Collectors.toList());
        authorities.forEach(authority -> log.info("Role: ".concat(authority.getAuthority())));

        if (authorities.isEmpty()) {
            log.error("Error en el Login: Usuario {} no tiene roles asignados!", username);
            throw new UsernameNotFoundException("Error en el Login: usuario " + username + " no tiene roles asignados!");
        }
        return new User(customer.get().getUsername(), customer.get().getPassword(), customer.get().getStatus(), true, true, true, authorities);
    }

    @Override
    public Optional<CustomerEntity> findCustomerByUsername(String username) {
        return repository.findCustomerByUsername(username);
    }

    @Override
    @Transactional
    public CustomerResponse processSaveCustomer(CustomerRequest request) throws ExceptionManager {
        try {
            PersonEntity person = PersonEntity.builder()
                    .name(request.getName())
                    .lastname(request.getLastname())
                    .identification(request.getIdentification())
                    .age(request.getAge())
                    .address(request.getAddress())
                    .telephone(request.getTelephone())
                    .gender(request.getGender())
                    .build();

            person.setStatus(request.getStatus());
            person.setCreationUser(CREATION_USER);
            person.setCreationHost(CREATION_HOST);
            person.setCreationDate(Date.from(Instant.now()));
            personService.save(person);

            CustomerEntity customer = CustomerEntity.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .person(person)
                    .build();

            customer.setStatus(request.getStatus());
            customer.setCreationUser(CREATION_USER);
            customer.setCreationHost(CREATION_HOST);
            customer.setCreationDate(Date.from(Instant.now()));
            super.save(customer);

            return CustomerResponse.builder()
                    .name(person.getName())
                    .lastname(person.getLastname())
                    .address(person.getAddress())
                    .telephone(person.getTelephone())
                    .identification(person.getIdentification())
                    .password(customer.getPassword())
                    .customerId(customer.getCustomerId())
                    .username(customer.getUsername())
                    .status(customer.getStatus()).build();
        } catch (Exception e) {
            log.error("processSaveCustomer: {0}", e);
            throw new ExceptionManager.GettingException("Error al guardar el registro");
        }
    }

    @Override
    @Transactional
    public CustomerResponse processUpdateCustomer(CustomerRequest request) throws ExceptionManager {
        try {
            Optional<CustomerEntity> customer = repository.findCustomerByIdentification(request);

            return customer.map(value -> this.updateCustomer(value, request)).orElse(null);
        } catch (Exception e) {
            log.error("processUpdateCustomer: {0}", e);
            throw new ExceptionManager.GettingException("Error al actualizar el registro");
        }
    }

    @Override
    public List<CustomerResponse> findCustomerAll() throws ExceptionManager {
        try {
            List<CustomerResponse> customerResponses = new ArrayList<>();
            List<CustomerEntity> customers = repository.findAll();
            customers.forEach(it -> customerResponses.add(CustomerResponse.builder()
                    .name(it.getPerson().getName())
                    .lastname(it.getPerson().getLastname())
                    .address(it.getPerson().getAddress())
                    .telephone(it.getPerson().getTelephone())
                    .identification(it.getPerson().getIdentification())
                    .password(it.getPassword())
                    .status(it.getStatus())
                    .username(it.getUsername())
                    .customerId(it.getCustomerId())
                    .build()));
            return customerResponses;
        } catch (Exception e) {
            log.error("findCustomerAll: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }

    @Override
    @Transactional
    public Long deleteCustomerById(Long id) throws ExceptionManager {
        try {
            Optional<CustomerEntity> customer = repository.findById(id);

            if (customer.isPresent()) {
                repository.deleteById(customer.get().getCustomerId());
                personService.deleteById(customer.get().getPerson().getPersonId());
                return 1L;
            }
            return 0L;
        } catch (ExceptionManager e) {
            log.error("deleteCustomerById: {0}", e);
            throw new ExceptionManager.DeletingException("Error al eliminar el registro");
        }
    }

    /**
     * Update customer
     *
     * @param customer
     * @param request
     * @return
     */
    private CustomerResponse updateCustomer(CustomerEntity customer, CustomerRequest request) {
        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPerson(customer.getPerson());

        customer.setStatus(request.getStatus());
        customer.setModificationUser(MODIFICATION_USER);
        customer.setModificationHost(MODIFICATION_HOST);
        customer.setModificationDate(Date.from(Instant.now()));
        super.update(customer);

        PersonEntity person = customer.getPerson();
        person.setName(request.getName());
        person.setLastname(request.getLastname());
        person.setIdentification(request.getIdentification());
        person.setAge(request.getAge());
        person.setAddress(request.getAddress());
        person.setTelephone(request.getTelephone());
        person.setGender(request.getGender());

        person.setStatus(request.getStatus());
        person.setModificationUser(MODIFICATION_USER);
        person.setModificationHost(MODIFICATION_HOST);
        person.setModificationDate(Date.from(Instant.now()));
        personService.update(person);

        return CustomerResponse.builder()
                .name(person.getName())
                .lastname(person.getLastname())
                .address(person.getAddress())
                .telephone(person.getTelephone())
                .identification(person.getIdentification())
                .password(customer.getPassword())
                .status(customer.getStatus())
                .customerId(customer.getCustomerId())
                .username(customer.getUsername())
                .build();

    }

    @Override
    public CustomerResponse findCustomerByIdentification(CustomerRequest request) throws ExceptionManager {
        try {
            Optional<CustomerEntity> customer = repository.findCustomerByIdentification(request);

            return customer.map(customerEntity -> CustomerResponse.builder()
                    .name(customerEntity.getPerson().getName())
                    .lastname(customerEntity.getPerson().getLastname())
                    .address(customerEntity.getPerson().getAddress())
                    .telephone(customerEntity.getPerson().getTelephone())
                    .password(customerEntity.getPassword())
                    .status(customerEntity.getStatus())
                    .customerId(customerEntity.getCustomerId())
                    .username(customerEntity.getUsername())
                    .build()).orElse(null);
        } catch (Exception e) {
            log.error("findCustomerByIdentification: {0}", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
