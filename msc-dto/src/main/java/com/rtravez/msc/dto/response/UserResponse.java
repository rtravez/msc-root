package com.rtravez.msc.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long userId;
    private String username;
    private String name;
    private String lastname;
    private String address;
    private String telephone;
    private String password;
    private String identification;
    private boolean status;


}