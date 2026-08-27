package com.rtravez.msc.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends PersonRequest {
    private Long userId;
    @NotBlank
    @Size(min = 8, max = 60)
    private String password;
    @NotBlank
    @Size(max = 20)
    private String username;
}