package com.yx.util;

public class CheckUtils {
    public static boolean checkString(String s) {
        for (int i = 0; i < s.length();i++)
            if (!Character.isDigit(s.charAt(i)) && !Character.isAlphabetic(s.charAt(i)) )
                return false;
        return true;
    }

    public static boolean checkStringDigit(String s) {
        for (int i = 0; i < s.length();i++)
            if (!Character.isDigit(s.charAt(i)) )
                return false;
        return true;
    }

    public static boolean checkStringAlphabetic(String s) {
        for (int i = 0; i < s.length();i++)
            if (!Character.isAlphabetic(s.charAt(i)) )
                return false;
        return true;
    }
}
