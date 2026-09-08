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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;

/**
 * UserController
 */
@RestController()
@RequestMapping("/api/users")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones de administración de usuarios")
public class UserController {

    private final UserService userService;
    private final PersonService personService;

    /**
     * Find user all
     *
     * @return
     */
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene los usuarios activos con paginación.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuarios consultados correctamente")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<Page<UserResponse>>> findUserAll(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> userResponses = userService.findUserAll(pageable);
        if (userResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<UserResponse>>builder()
                    .code(HttpStatus.OK.value()).message("No existen usuarios").build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponseDto.<Page<UserResponse>>builder().code(HttpStatus.OK.value())
                        .data(userResponses).message("Usuarios encontrados con \u00E9xito").build());
    }

    /**
     * Find user by identification
     *
     * @param request
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @GetMapping(params = "identification")
    @Operation(summary = "Buscar usuario por identificación", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> findUserByIdentification(
            @Parameter(description = "Número de identificación del usuario", required = true, in = ParameterIn.QUERY, example = "1712345678") @RequestParam String identification) {
        UserResponse userResponse = this.userService.findUserByIdentification(identification);
        if (userResponse == null || userResponse.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<UserResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value()).message("Usuario no encontrado").build());
        }
        return ResponseEntity.ok(BaseResponseDto.<UserResponse>builder().code(HttpStatus.OK.value()).data(userResponse)
                .message("Usuario encontrado con \u00E9xito").build());
    }

    @Secured({ "ROLE_ADMIN" })
    @GetMapping(path = "/{id}")
    @Operation(summary = "Buscar usuario por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> findUserById(
            @Parameter(description = "Identificador del usuario", required = true, example = "1") @PathVariable Long id) {
        UserResponse userResponse = this.userService.findUserById(id);
        if (userResponse == null || userResponse.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<UserResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value()).message("Usuario no encontrado").build());
        }
        return ResponseEntity.ok(BaseResponseDto.<UserResponse>builder().code(HttpStatus.OK.value()).data(userResponse)
                .message("Usuario encontrado con \u00E9xito").build());
    }

    /**
     * Save user
     *
     * @param request
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un usuario y su información personal.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "La identificación ya existe")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> save(@Valid @RequestBody UserRequest request) {
        if (Boolean.TRUE.equals(this.personService.exist(request.getIdentification()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponseDto.<UserResponse>builder()
                    .code(HttpStatus.CONFLICT.value()).message("El usuario ya existe").build());
        }

        UserResponse response = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.<UserResponse>builder()
                .code(HttpStatus.CREATED.value()).data(response).message("Usuario creado con \u00E9xito").build());
    }

    /**
     * Update user
     *
     * @param request
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @PutMapping(path = "/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos del usuario indicado.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Conflicto de integridad")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> update(
            @Parameter(description = "Identificador del usuario", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<UserResponse>builder()
                .code(HttpStatus.OK.value()).data(response).message("Usuario actualizado con \u00E9xito").build());
    }

    /**
     * Delete user by id
     *
     * @param id
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Eliminar usuario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<Long>> deleteById(
            @Parameter(description = "Identificador del usuario", required = true, example = "1") @PathVariable Long id) {
        if (this.userService.deleteUserById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Long>builder().code(HttpStatus.OK.value())
                    .message("Usuario eliminado con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<Long>builder()
                    .code(HttpStatus.NOT_FOUND.value()).message("El usuario no existe").build());
        }
    }
}
