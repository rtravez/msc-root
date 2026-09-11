package com.rtravez.msc.controller;

import com.rtravez.msc.dto.BaseResponseDto;
import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.service.PersonService;
import com.rtravez.msc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
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

/**
 * UserController
 */
@RestController()
@RequestMapping("/api/users")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones de administración de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final PersonService personService;

    /**
     * Retrieves a paginated list of active users.
     *
     * @param pageable the pagination and sorting information. Defaults to 20 records per page.
     * @return a {@link ResponseEntity} containing a {@link BaseResponseDto} with the paginated list of {@link UserResponse}.
     * The response includes status and message information. If no users are found, the response will contain a message
     * indicating that there are no users.
     */
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene los usuarios activos con paginación.")
    @ApiResponse(responseCode = "200", description = "Usuarios consultados correctamente")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<Page<UserResponse>>> findUserAll(
            @Parameter(description = "Paginación y ordenamiento. Por defecto devuelve 20 registros por página.")
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> userResponses = userService.findUserAll(pageable);
        if (userResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<UserResponse>>builder()
                    .status(HttpStatus.OK.value()).detail("No existen usuarios").build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponseDto.<Page<UserResponse>>builder().status(HttpStatus.OK.value())
                        .data(userResponses).detail("Usuarios encontrados con éxito").build());
    }

    /**
     * Find user by identification
     *
     * @param identification
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @GetMapping(path = "identification", params = "identification")
    @Operation(summary = "Listar usuario por identificación", description = "Obtiene un usuario por su identificación")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> findUserByIdentification(
            @Parameter(description = "Número de identificación del usuario", required = true, in = ParameterIn.QUERY, example = "1712345678") @RequestParam String identification) {
        return getBaseResponseDtoResponseEntity(this.userService.findUserByIdentification(identification));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Buscar usuario por id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> findUserById(
            @Parameter(description = "Identificador del usuario", required = true, example = "1") @PathVariable Long id) {
        return getBaseResponseDtoResponseEntity(this.userService.findUserById(id));
    }

    /**
     * Save user
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un usuario y su información personal.")
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "La identificación ya existe")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> save(@Valid @RequestBody UserRequest request) {
        if (Boolean.TRUE.equals(this.personService.exist(request.getIdentification()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponseDto.<UserResponse>builder()
                    .status(HttpStatus.CONFLICT.value()).detail("El usuario ya existe").build());
        }

        UserResponse response = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.<UserResponse>builder()
                .status(HttpStatus.CREATED.value()).data(response).detail("Usuario creado con éxito").build());
    }

    /**
     * Update user
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos del usuario indicado.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Conflicto de integridad")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<UserResponse>> update(
            @Parameter(description = "Identificador del usuario", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<UserResponse>builder()
                .status(HttpStatus.OK.value()).data(response).detail("Usuario actualizado con éxito").build());
    }

    /**
     * Delete user by id
     *
     * @param id
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Eliminar usuario")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<Long>> deleteById(
            @Parameter(description = "Identificador del usuario", required = true, example = "1") @PathVariable Long id) {
        if (this.userService.deleteUserById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Long>builder().status(HttpStatus.OK.value())
                    .detail("Usuario eliminado con éxito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<Long>builder()
                    .status(HttpStatus.NOT_FOUND.value()).detail("El usuario no existe").build());
        }
    }

    @NonNull
    private ResponseEntity<BaseResponseDto<UserResponse>> getBaseResponseDtoResponseEntity(UserResponse response) {
        if (response == null || response.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<UserResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value()).detail("Usuario no encontrado").build());
        }
        return ResponseEntity.ok(BaseResponseDto.<UserResponse>builder().status(HttpStatus.OK.value()).data(response)
                .detail("Usuario encontrado con éxito").build());
    }
}
