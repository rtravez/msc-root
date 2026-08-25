package com.sofka.msc.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String CREATION_USER = "admin";
    public static final String CREATION_HOST = "127.0.0.1";
    public static final String MODIFICATION_USER = "admin";
    public static final String MODIFICATION_HOST = "127.0.0.1";
    public static final String USERNAME_CLIENT = "sofka-web";
    public static final String PASSWORD_CLIENT = "$2a$10$xeNMEgFFjIDQci6XOtxtdOcC5IQMguGO/b1Igy86ANf7XmfpmhHDG";
    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
}
