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
     * @param userEntity the user entity
     * @return the user response DTO
     */
    @Mapping(target = "name", source = "userEntity.person.name")
    @Mapping(target = "lastname", source = "userEntity.person.lastname")
    @Mapping(target = "identification", source = "userEntity.person.identification")
    @Mapping(target = "address", source = "userEntity.person.address")
    @Mapping(target = "telephone", source = "userEntity.person.telephone")
    @Mapping(target = "gender", source = "userEntity.person.gender")
    @Mapping(target = "age", source = "userEntity.person.age")
    UserResponse toResponse(UserEntity userEntity);

    /**
     * Maps UserRequest to PersonEntity.
     * Extracts only the person-related fields from user request.
     * Service layer is responsible for setting audit/metadata fields.
     *
     * @param userRequest the user request DTO
     * @return the person entity with populated person fields
     */
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    PersonEntity toEntity(UserRequest userRequest);

}
