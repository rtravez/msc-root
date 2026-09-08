package com.rtravez.msc.dto.response;

import com.rtravez.msc.dto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Class VO for a person process.
 *
 * @author components on 2021/07/29.
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse extends BaseDto {
    private Long personId;
    private String identification;
    private String name;
    private String lastname;
    private String address;
    private String telephone;
    private Character gender;
    private Integer age;
}
