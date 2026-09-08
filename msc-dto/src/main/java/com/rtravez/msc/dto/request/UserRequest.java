package com.rtravez.msc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends PersonRequest {
    private Long userId;
    @NotBlank
    @Size(min = 8, max = 60, message = "la contraseña debe tener entre 8 y 60 caracteres")
    private String password;
    @NotBlank
    @Size(max = 20, message = "el nombre de usuario debe tener como máximo 20 caracteres")
    private String username;
}