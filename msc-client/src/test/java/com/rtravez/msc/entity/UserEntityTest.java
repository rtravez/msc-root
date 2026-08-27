package com.rtravez.msc.entity;

import com.rtravez.msc.entity.view.AccountView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Este ejemplo se centra en probar la correcta construcción de la entidad y la asignación de sus atributos
 */
class UserEntityTest {

    private UserEntity user;

    @BeforeEach
    void setUp() {
        // Mocking dependencies
        PersonEntity person = mock(PersonEntity.class);
        AccountView accountView1 = mock(AccountView.class);
        AccountView accountView2 = mock(AccountView.class);

        // Creating lists of roles and accounts
        List<AccountView> accounts = Arrays.asList(accountView1, accountView2);

        // Constructing UserEntity
        user = UserEntity.builder()
                .userId(1L)
                .username("testUser")
                .password("testPass")
                .person(person)
                .build();
    }

    @Test
    void testUserEntityCreation() {
        assertEquals(1L, user.getUserId());
        assertEquals("testUser", user.getUsername());
        assertEquals("testPass", user.getPassword());
    }
}
