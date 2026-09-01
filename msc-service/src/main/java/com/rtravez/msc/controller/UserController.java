package com.rtravez.msc.controller;

import com.rtravez.msc.dto.BaseResponseDto;
import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.service.IUserService;
import com.rtravez.msc.service.IPersonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * UserController
 */
@RestController()
@RequestMapping("/api/users")
@Validated
@Slf4j
public class UserController {

    private final IUserService userService;
    private final IPersonService personService;

    public UserController(IUserService userService, IPersonService personService) {
        this.userService = userService;
        this.personService = personService;
    }

    /**
     * Find user all
     *
     * @return
     */
    @GetMapping
    @Operation(summary = "Find User")
    public ResponseEntity<BaseResponseDto<List<UserResponse>>> findUserAll() {
        List<UserResponse> userResponses = userService.findUserAll();
        if (userResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<List<UserResponse>>builder().code(HttpStatus.OK.value()).message("No existen usuarios").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<List<UserResponse>>builder().code(HttpStatus.OK.value())
                .data(userResponses).message("Usuarios encontrados con \u00E9xito").build());
    }

    /**
     * Find user by identification
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PostMapping(path = "findUserByIdentification")
    @Operation(summary = "Find user by identification")
    public ResponseEntity<UserResponse> findUserByIdentification(@Valid @RequestBody UserRequest request) {
        UserResponse response = this.userService.findUserByIdentification(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Save user
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @Operation(summary = "Create User")
    public ResponseEntity<BaseResponseDto<Object>> save(@Valid @RequestBody UserRequest request) {
        if (Boolean.TRUE.equals(this.personService.exist(request.getIdentification()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponseDto.builder().code(HttpStatus.CONFLICT.value()).message("El usuario ya existe").build());
        }

        UserResponse response = userService.processSaveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.builder().code(HttpStatus.CREATED.value()).data(response).message("Usuario creado con \u00E9xito").build());
    }

    /**
     * Update user
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PutMapping
    @Operation(summary = "Update User")
    public ResponseEntity<BaseResponseDto<Object>> update(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.processUpdateUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).data(response).message("Usuario actualizado con \u00E9xito").build());
    }

    /**
     * Delete user by id
     *
     * @param id
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete User")
    public ResponseEntity<BaseResponseDto<Object>> deleteById(@PathVariable Long id) {
        if (this.userService.deleteUserById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("Usuario eliminado con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.builder().code(HttpStatus.NOT_FOUND.value()).message("El usuario no existe").build());
        }
    }
}
