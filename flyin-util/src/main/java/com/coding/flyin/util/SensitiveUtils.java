package com.coding.flyin.util;

import com.coding.flyin.cmp.common.regex.RegexDefine;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class SensitiveUtils {

    /** 字符串脱敏处理. */
    public static String desensitization(String sensitiveStr) {
        String desenStr;
        if (sensitiveStr == null) {
            desenStr = "";
        } else if (RegexDefine.MOBILE_REGEX_PATTERN.matcher(sensitiveStr).matches()) {
            desenStr =
                    RegExUtils.replacePattern(sensitiveStr, "(\\d{3})(\\d*)(\\d{4})", "$1****$3");
        } else {
            sensitiveStr = StringUtils.rightPad(sensitiveStr, 3);
            String middleStr = StringUtils.repeat("*", sensitiveStr.length() - 2);
            String replacement = "$1" + middleStr + "$3";
            desenStr = RegExUtils.replacePattern(sensitiveStr, "(.)(.*)(.{1})", replacement);
        }
        return desenStr.trim();
    }

    public static void main(String[] args) {
        System.out.println(desensitization("a"));
        System.out.println(desensitization("ab"));
        System.out.println(desensitization("abc"));
        System.out.println(desensitization("abcd"));
        System.out.println(desensitization("abcdefghijklmn"));
        System.out.println(desensitization("18767188240"));
    }
}
