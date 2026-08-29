package com.rtravez.msc.mapper;

import com.rtravez.msc.dto.request.UserRequest;
import com.rtravez.msc.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for UserRequest to PersonEntity conversions.
 * Extracts person data from user request DTOs.
 * Note: Audit fields (status, hosts, users, dates) must be set by the service layer.
 *
 * @author renetravez
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserRequestToPersonMapper {

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
    PersonEntity userRequestToPersonEntity(UserRequest userRequest);

}
