package com.sofka.msc.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerRequest extends PersonRequest {
    private Long customerId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String username;
}