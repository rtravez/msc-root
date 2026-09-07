package com.rtravez.msc.mapper;

import com.rtravez.msc.dto.request.PersonRequest;
import com.rtravez.msc.dto.response.PersonResponse;
import com.rtravez.msc.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    PersonResponse toResponse(PersonEntity personEntity);

    /**
     * Maps a person request to an entity.
     *
     * @param request the person request
     * @return the person entity
     */
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    PersonEntity toEntity(PersonRequest request);

    /**
     * Maps PersonDto to PersonEntity.
     *
     * @param personDto the person DTO
     * @return the person entity
     */
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    PersonEntity toEntity(PersonResponse response);

}
