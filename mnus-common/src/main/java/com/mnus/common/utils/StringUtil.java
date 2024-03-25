package com.mnus.common.utils;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/25 18:22:55
 */
public class StringUtil {
    public static String contactWithUnderline(String... strings) {
        StringBuilder res = new StringBuilder();
        for (String string : strings) {
            res.append(string).append("_");
        }
        return res.substring(0, res.length() - 1);
    }
}
