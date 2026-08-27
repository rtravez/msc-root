package com.rtravez.msc.dto.request;

import com.rtravez.msc.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonRequest extends BaseDto {

    private Long personId;
    @NotBlank
    @Size(max = 10)
    @Pattern(regexp = "\\d+", message = "debe contener solo dígitos")
    private String identification;
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 255)
    private String lastname;
    @Size(max = 255)
    private String address;
    @Pattern(regexp = "\\d{10}", message = "debe contener 10 dígitos")
    private String telephone;
    private Character gender;
    @Min(0)
    @Max(150)
    private Integer age;
}
