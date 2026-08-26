package com.sofka.msc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Class VO for person process.
 *
 * @author components on 2021/07/29.
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto extends BaseDto {
    private Long personId;
    private String identification;
    private String name;
    private String lastname;
    private String address;
    private String telephone;
    private Character gender;
    private Integer age;
}
