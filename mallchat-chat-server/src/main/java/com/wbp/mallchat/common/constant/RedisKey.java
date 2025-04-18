package com.wbp.mallchat.common.constant;

public class RedisKey {
    private static final String BASE_KEY = "mallchat:chat";
    /**
     *  用户token
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getBaseKey (String key, Object... o) {
        return BASE_KEY +String.format(key, o);
    }
}
