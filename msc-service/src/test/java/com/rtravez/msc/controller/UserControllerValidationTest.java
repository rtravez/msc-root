package com.rtravez.msc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.rtravez.msc.controller.exception.GlobalExceptionHandler;
import com.rtravez.msc.service.PersonService;
import com.rtravez.msc.service.UserService;

class UserControllerValidationTest {

    private final UserService userService = org.mockito.Mockito.mock(UserService.class);
    private final PersonService personService = org.mockito.Mockito.mock(PersonService.class);

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, personService))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void save_whenRequestBodyContainsWrongType_returnsBadRequest() throws Exception {
        String invalidJson = """
                {
                  "identification": "1712345678",
                  "name": "Ana",
                  "lastname": "Lopez",
                  "password": "123",
                  "username": "ana_lopez",
                  "age": "invalid"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.errors").exists());
    }
}
