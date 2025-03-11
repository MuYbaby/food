package com.muybaby.food.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoderUtil {

    /**
     * 对密码进行加密
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 验证密码是否匹配
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 密码是否匹配
     */
    public static boolean checkPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        String password = "HG666666";
        String encodedPassword = PasswordEncoderUtil.encodePassword(password);

        System.out.println("Encoded password: " + encodedPassword);

        boolean isMatch = PasswordEncoderUtil.checkPassword(password, encodedPassword);

        System.out.println("Passwords match: " + isMatch);
    }
}
