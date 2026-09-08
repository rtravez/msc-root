package com.rtravez.msc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.dto.response.UserResponse;
import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.entity.UserEntity;

/**
 * MapStruct mapper for UserEntity to UserResponse conversions.
 * Centralizes DTO mapping logic and eliminates manual conversion code.
 *
 * @author renetravez
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps UserEntity to UserResponse.
     * Includes nested Person data extraction.
     *
     * @param entity the user entity
     * @return the user response DTO
     */
    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "lastname", source = "person.lastname")
    @Mapping(target = "identification", source = "person.identification")
    @Mapping(target = "address", source = "person.address")
    @Mapping(target = "telephone", source = "person.telephone")
    @Mapping(target = "gender", source = "person.gender")
    @Mapping(target = "age", source = "person.age")
    UserResponse toResponse(UserEntity entity);

    /**
     * Maps UserRequest to PersonEntity.
     * Extracts only the person-related fields from the user request.
     * The service layer is responsible for setting audit/metadata fields.
     *
     * @param request the user request DTO
     * @return the person entity with populated person fields
     */
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    PersonEntity toEntity(UserRequest request);

}
