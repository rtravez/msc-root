package com.sofka.msc.dto.request;

import com.sofka.msc.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

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
