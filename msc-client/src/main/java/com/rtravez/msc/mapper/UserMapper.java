package com.rtravez.msc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rtravez.msc.dto.response.UserResponse;
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
    UserResponse userEntityToUserResponse(UserEntity userEntity);

}
