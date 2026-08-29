package com.rtravez.msc.mapper;

import com.rtravez.msc.dto.PersonDto;
import com.rtravez.msc.entity.PersonEntity;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for PersonEntity to DTO conversions.
 * Handles entity-to-DTO transformations with automatic field mapping.
 *
 * @author renetravez
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    /**
     * Maps PersonEntity to PersonDto.
     *
     * @param personEntity the person entity
     * @return the person DTO
     */
    PersonDto personEntityToPersonDto(PersonEntity personEntity);

    /**
     * Maps PersonDto to PersonEntity.
     *
     * @param personDto the person DTO
     * @return the person entity
     */
    PersonEntity personDtoToPersonEntity(PersonDto personDto);

}
