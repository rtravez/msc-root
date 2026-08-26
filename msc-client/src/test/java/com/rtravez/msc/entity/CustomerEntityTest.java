package com.sofka.msc.entity;

import com.sofka.msc.entity.common.RoleCustomerEntity;
import com.sofka.msc.entity.view.AccountView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Este ejemplo se centra en probar la correcta construcción de la entidad y la asignación de sus atributos
 */
class CustomerEntityTest {

    private CustomerEntity customer;

    @BeforeEach
    public void setUp() {
        // Mocking dependencies
        PersonEntity person = mock(PersonEntity.class);
        RoleCustomerEntity roleCustomer1 = mock(RoleCustomerEntity.class);
        RoleCustomerEntity roleCustomer2 = mock(RoleCustomerEntity.class);
        AccountView accountView1 = mock(AccountView.class);
        AccountView accountView2 = mock(AccountView.class);

        // Creating lists of roles and accounts
        List<RoleCustomerEntity> roleCustomers = Arrays.asList(roleCustomer1, roleCustomer2);
        List<AccountView> accounts = Arrays.asList(accountView1, accountView2);

        // Constructing CustomerEntity
        customer = CustomerEntity.builder()
                .customerId(1L)
                .username("testUser")
                .password("testPass")
                .person(person)
                .roleCustomers(roleCustomers)
                .accounts(accounts)
                .build();
    }

    @Test
    void testCustomerEntityCreation() {
        assertEquals(1L, customer.getCustomerId());
        assertEquals("testUser", customer.getUsername());
        assertEquals("testPass", customer.getPassword());
        assertEquals(2, customer.getRoleCustomers().size());
        assertEquals(2, customer.getAccounts().size());
    }
}
