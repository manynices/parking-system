package com.experiment.parkingsystem.common;

/**
 * 使用 ThreadLocal 存储当前登录的用户 ID
 */
public class UserContext {
    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    public static void remove() {
        USER_ID_HOLDER.remove();
    }
}