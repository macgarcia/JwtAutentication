package com.macgarcia.senhasegura.jwt;

public class SecurityConstants {

    public static final String SECRET = "macgarcia";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    public static final String SING_UP_URL = "/sign-up";

    //** Expira em um dia o token **//
    public static final long EXPIRATION_TIME = 86400000L;

}
