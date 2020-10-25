package com.coding.flyin.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public abstract class SensitiveUtils {

    /**
     * 用户手机号正则表达式定义.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180424 15:41</font><br>
     *
     * <p>匹配校验手机号的合法性，并能获取手机号开头3个数字，与尾号4个数字
     *
     * <p>正则：手机号（精确）
     *
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、172、178、182、183、184、187、188、198
     *
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166
     *
     * <p>电信：133、153、173、177、180、181、189、191、199
     *
     * <p>全球星：1349
     *
     * <p>虚拟运营商：170
     *
     * <ul>
     *   <li>正则：^((?:13[0-9])|(?:14[5|7|9])|(?:15(?:[0-3]|[5-9]))|(?:16[6])|(?:17[01235678])|(?:18[0-9])|(?:19[189]))\d{4}(\d{4})$
     *   <li>定义：11位手机号码
     *   <li>示例：18767188240
     * </ul>
     *
     * @since 1.0.0
     */
    private static final String MOBILE_REGEX =
            "^((?:13[0-9])|(?:14[5|7|9])|(?:15(?:[0-3]|[5-9]))|(?:16[6])|(?:17[01235678])|(?:18[0-9])|(?:19[189]))\\d{4}(\\d{4})$";

    /** 字段说明：{@linkplain #MOBILE_REGEX}. */
    private static final Pattern MOBILE_REGEX_PATTERN = Pattern.compile(MOBILE_REGEX);

    /** 字符串脱敏处理. */
    public static String desensitization(String sensitiveStr) {
        String desenStr;
        if (sensitiveStr == null) {
            desenStr = "";
        } else if (MOBILE_REGEX_PATTERN.matcher(sensitiveStr).matches()) {
            desenStr =
                    StringUtils.replacePattern(sensitiveStr, "(\\d{3})(\\d*)(\\d{4})", "$1****$3");
        } else {
            sensitiveStr = StringUtils.rightPad(sensitiveStr, 3);
            String middleStr = StringUtils.repeat("*", sensitiveStr.length() - 2);
            String replacement = "$1" + middleStr + "$3";
            desenStr = StringUtils.replacePattern(sensitiveStr, "(.)(.*)(.{1})", replacement);
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
