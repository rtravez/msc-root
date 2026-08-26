package com.rtravez.msc.dto.request;

import com.rtravez.msc.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonRequest extends BaseDto {

    private Long personId;
    @NotEmpty
    private String identification;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    private String address;
    private String telephone;
    private Character gender;
    private Integer age;
}
