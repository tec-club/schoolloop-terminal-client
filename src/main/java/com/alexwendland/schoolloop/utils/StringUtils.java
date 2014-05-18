package com.alexwendland.schoolloop.utils;

import java.util.List;

/**
 * Created by awendland on 5/17/14.
 */
public class StringUtils {

    public static String concatToString(List<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s);
            sep = separator;
        }
        return sb.toString();
    }
}
