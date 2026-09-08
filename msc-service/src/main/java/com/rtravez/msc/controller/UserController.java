package com.rtravez.msc.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rtravez.msc.dto.BaseResponseDto;
import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.service.PersonService;
import com.rtravez.msc.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserController
 */
@RestController()
@RequestMapping("/api/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PersonService personService;   

    /**
     * Find user all
     *
     * @return
     */
    @GetMapping
    @Operation(summary = "Find User")
    public ResponseEntity<BaseResponseDto<Page<UserResponse>>> findUserAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> userResponses = userService.findUserAll(pageable);
        if (userResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<UserResponse>>builder().code(HttpStatus.OK.value()).message("No existen usuarios").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<UserResponse>>builder().code(HttpStatus.OK.value())
                .data(userResponses).message("Usuarios encontrados con \u00E9xito").build());
    }

    /**
     * Find user by identification
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @GetMapping(params = "identification")
    @Operation(summary = "Find user by identification")
    public ResponseEntity<BaseResponseDto<UserResponse>> findUserByIdentification(@RequestParam String identification) {
        UserResponse userResponse = this.userService.findUserByIdentification(identification);
        if (userResponse == null || userResponse.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<UserResponse>builder().code(HttpStatus.NOT_FOUND.value()).message("Usuario no encontrado").build());
        }
        return ResponseEntity.ok(BaseResponseDto.<UserResponse>builder().code(HttpStatus.OK.value()).data(userResponse).message("Usuario encontrado con \u00E9xito").build());
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Find User by id")
    public ResponseEntity<BaseResponseDto<UserResponse>> findUserById(@PathVariable Long id) {
        UserResponse userResponse = this.userService.findUserById(id);
        if (userResponse == null || userResponse.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<UserResponse>builder().code(HttpStatus.NOT_FOUND.value()).message("Usuario no encontrado").build());
        }
        return ResponseEntity.ok(BaseResponseDto.<UserResponse>builder().code(HttpStatus.OK.value()).data(userResponse).message("Usuario encontrado con \u00E9xito").build());
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
    public ResponseEntity<BaseResponseDto<UserResponse>> save(@Valid @RequestBody UserRequest request) {
        if (Boolean.TRUE.equals(this.personService.exist(request.getIdentification()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponseDto.<UserResponse>builder().code(HttpStatus.CONFLICT.value()).message("El usuario ya existe").build());
        }

        UserResponse response = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.<UserResponse>builder().code(HttpStatus.CREATED.value()).data(response).message("Usuario creado con \u00E9xito").build());
    }

    /**
     * Update user
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update User")
    public ResponseEntity<BaseResponseDto<UserResponse>> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<UserResponse>builder().code(HttpStatus.OK.value()).data(response).message("Usuario actualizado con \u00E9xito").build());
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
    public ResponseEntity<BaseResponseDto<Long>> deleteById(@PathVariable Long id) {
        if (this.userService.deleteUserById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Long>builder().code(HttpStatus.OK.value()).message("Usuario eliminado con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<Long>builder().code(HttpStatus.NOT_FOUND.value()).message("El usuario no existe").build());
        }
    }
}
