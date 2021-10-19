package com.coding.flyin.cmp.common.regex;

import java.util.regex.Pattern;

/**
 * 正则表达式定义类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180424 14:56</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
public interface RegexDefine {

    /**
     * 示例1：完全匹配的正则表达式定义.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180424 15:19</font><br>
     *
     * <ul>
     *   <li>正则：^(\$R)\.(\w+)$
     *   <li>定义：$R.key
     *   <li>示例：$R.TransCode
     * </ul>
     *
     * @since 0.1.0
     */
    String $R_REGEX = "^(\\$R)\\.(\\w+)$";

    Pattern $R_REGEX_PATTERN = Pattern.compile($R_REGEX);

    /**
     * 示例2：部分匹配的正则表达式定义.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180424 15:24</font><br>
     *
     * <ul>
     *   <li>正则：{}
     *   <li>定义：{}
     *   <li>示例：{}决定重新改写{}日志的格式化方式为{}
     * </ul>
     *
     * @since 0.1.0
     */
    String LOG_FORMATER_REGEX = "\\{\\}";

    Pattern LOG_FORMATER_REGEX_PATTERN = Pattern.compile(LOG_FORMATER_REGEX);

    // ==================================================华丽的分割线==================================================

    /**
     * 用户手机号正则表达式定义.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180424 15:41</font><br>
     * 匹配校验手机号的合法性，并能获取手机号开头3个数字，与尾号4个数字<br>
     * 正则：手机号（精确）<br>
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、172、178、182、183、184、187、188、195、198
     * <br>
     * 联通：130、131、132、145、155、156、175、176、185、186、166<br>
     * 电信：133、153、173、177、180、181、189、191、199<br>
     * 全球星：1349<br>
     * 虚拟运营商：170<br>
     *
     * <ul>
     *   <li>正则：^((?:13[0-9])|(?:14[5|7|9])|(?:15(?:[0-3]|[5-9]))|(?:16[6])|(?:17[01235678])|(?:18[0-9])|(?:19[1589]))\d{4}(\d{4})$
     *   <li>定义：11位手机号码
     *   <li>示例：18767188240
     * </ul>
     *
     * @since 0.1.0
     */
    String MOBILE_REGEX =
            "^((?:13[0-9])|(?:14[5|7|9])|(?:15(?:[0-3]|[5-9]))|(?:16[6])|(?:17[01235678])|(?:18[0-9])|(?:19[1589]))\\d{4}(\\d{4})$";
    /** 字段说明：{@linkplain #MOBILE_REGEX}. */
    Pattern MOBILE_REGEX_PATTERN = Pattern.compile(MOBILE_REGEX);

    // (((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$
    String URL_REGEX =
            "(^(https?)://(?:[\\w:.-]+)?((?:\\/[\\+~%\\/.\\w-_#]*)?))\\??([-\\+=&;%@.\\w_:]*)#?(?:[\\w]*)$";
    Pattern URL_REGEX_PATTERN = Pattern.compile(URL_REGEX);

    String URI_REGEX = "^(https?)://(?:[\\w:.-]+)?((?:\\/[\\+~%\\/.\\w-_#]*)?)$";
    Pattern URI_REGEX_PATTERN = Pattern.compile(URI_REGEX);

    String URL_PARAM_REGEX = "\\??([-\\+=&;%@.\\w_]*)#?(?:[\\w]*)";
    Pattern URL_PARAM_REGEX_PATTERN = Pattern.compile(URL_PARAM_REGEX);

    /** 严格的文件名校验，需要完全匹配. */
    String STRICT_FILENAME_REGEX =
            "^(?:(?:([^<>/\\\\\\|:\"\"\\*\\?]+)\\.(\\w+)\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))|(?:([^<>/\\\\\\|:\"\"\\*\\?]+)))$";

    Pattern STRICT_FILENAME_REGEX_PATTERN = Pattern.compile(STRICT_FILENAME_REGEX);

    /** 不严格的文件名校验，不需要完全匹配. */
    String FILENAME_REGEX =
            "(?:(?:([^<>/\\\\\\|:\"\"\\*\\?]+)\\.(\\w+)\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))|(?:([^<>/\\\\\\|:\"\"\\*\\?]+)))$";

    Pattern FILENAME_REGEX_PATTERN = Pattern.compile(FILENAME_REGEX);

    /** 验证文件是否图片. */
    String IMAGE_REGEX =
            "([^<>/\\\\\\|:\"\"\\*\\?]+)\\.(jpg|JPG|jpeg|JPEG|gif|GIF|png|PNG)\\??([-\\+=&;%@.\\w_]*)#?(?:[\\w]*)$";

    Pattern IMAGE_REGEX_PATTERN = Pattern.compile(IMAGE_REGEX);

    /** 验证文件是否音频. */
    String AUDIO_REGEX =
            "([^<>/\\\\\\|:\"\"\\*\\?]+)\\.(mp3|MP3|wav|WAV|ape|APE|flac|m4a|aac)\\??([-\\+=&;%@.\\w_]*)#?(?:[\\w]*)$";

    Pattern AUDIO_REGEX_PATTERN = Pattern.compile(AUDIO_REGEX);

    /** 验证文件是否视频. */
    String VEDIO_REGEX =
            "([^<>/\\\\\\|:\"\"\\*\\?]+)\\.(mp4|avi|rmvb|flv|wmv|vob|mkv|mov)\\??([-\\+=&;%@.\\w_]*)#?(?:[\\w]*)$";

    Pattern VEDIO_REGEX_PATTERN = Pattern.compile(VEDIO_REGEX);

    // ==================================================华丽的分割线==================================================

    /** 邮箱地址校验规则. */
    String EMAIL_VALUE_REGEX =
            "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    /** {@linkplain #PARAM_VALUE_REGEX}. */
    Pattern EMAIL_VALUE_REGEX_PATTERN = Pattern.compile(EMAIL_VALUE_REGEX);

    /** 匹配中文字符的正则表达式. */
    String CN_REGEX = "^[\\u4e00-\\u9fa5]$";

    /** {@linkplain #CN_REGEX}. */
    Pattern CN_REGEX_PATTERN = Pattern.compile(CN_REGEX);

    /** 匹配双字节字符. */
    String TWO_BYTE_REGEX = "^[^\\x00-\\xff]$";

    /** {@linkplain #TWO_BYTE_REGEX}. */
    Pattern TWO_BYTE_REGEX_PATTERN = Pattern.compile(TWO_BYTE_REGEX);

    /** 匹配6-16位的单字节字符串. */
    String SIX_TO_SIXTEEN_ONE_BYTE_CHAR_REGEX = "^([\\x00-\\xff]){6,16}$";

    /** {@linkplain #SIX_TO_SIXTEEN_ONE_BYTE_CHAR_REGEX}. */
    Pattern SIX_TO_SIXTEEN_ONE_BYTE_CHAR_REGEX_PATTERN =
            Pattern.compile(SIX_TO_SIXTEEN_ONE_BYTE_CHAR_REGEX);

    /** 密码校验：必须包含数字+大写或小写字母的组合，6-8个. */
    String PASSWORD_REGEX = "^(?=.*[0-9].*)(?=.*[a-z].*|(?=.*[A-Z].*)).{6,8}$";

    Pattern PASSWORD_REGEX_PATTERN = Pattern.compile(PASSWORD_REGEX);

    /** 正整数. */
    String POSITIVE_INTEGER_REGEX = "^[1-9]\\d*$";

    Pattern POSITIVE_INTEGER_REGEX_PATTERN = Pattern.compile(POSITIVE_INTEGER_REGEX);

    /** 非负整数. */
    String NON_NEGATIVE_INTEGER_REGEX = "^[1-9]\\d*|0$";

    Pattern NON_NEGATIVE_INTEGER_REGEX_PATTERN = Pattern.compile(NON_NEGATIVE_INTEGER_REGEX);

    /** 非负浮点数，精度2位. */
    String NON_NEGATIVE_FLOATING_POINT_REGEX = "^(?:[1-9]\\d*(?:\\.\\d{1,2})?|0(?:\\.\\d{1,2})?)$";

    Pattern NON_NEGATIVE_FLOATING_POINT_REGEX_PATTERN =
            Pattern.compile(NON_NEGATIVE_FLOATING_POINT_REGEX);

    /** 非负浮点数，精度3位. */
    String NON_NEGATIVE_FLOATING_3_POINT_REGEX =
            "^(?:[1-9]\\d*(?:\\.\\d{1,3})?|0(?:\\.\\d{1,3})?)$";

    Pattern NON_NEGATIVE_FLOATING_3_POINT_REGEX_PATTERN =
            Pattern.compile(NON_NEGATIVE_FLOATING_3_POINT_REGEX);

    /** 区间[0-1)的浮点数，精度3位. */
    String NON_NEGATIVE_LESS_1_FLOATING_2_POINT_REGEX = "^0(?:\\.\\d{1,3})?$";

    Pattern NON_NEGATIVE_LESS_1_FLOATING_2_POINT_REGEX_PATTERN =
            Pattern.compile(NON_NEGATIVE_LESS_1_FLOATING_2_POINT_REGEX);

    /** 区间[0-1)的浮点数，精度2位. */
    String NON_NEGATIVE_LESS_1_FLOATING_3_POINT_REGEX = "^0(?:\\.\\d{1,3})?$";

    Pattern NON_NEGATIVE_LESS_1_FLOATING_3_POINT_REGEX_PATTERN =
            Pattern.compile(NON_NEGATIVE_LESS_1_FLOATING_3_POINT_REGEX);

    /** 区间[0-1]的浮点数，精度2位. */
    String NON_NEGATIVE_LESS_EQUAL_1_FLOATING_2_POINT_REGEX = "^0(?:\\.\\d{1,2})?|1(?:\\.0{1,2})?$";

    Pattern NON_NEGATIVE_LESS_EQUAL_1_FLOATING_2_POINT_REGEX_PATTERN =
            Pattern.compile(NON_NEGATIVE_LESS_EQUAL_1_FLOATING_2_POINT_REGEX);

    /** 区间[0-1]的浮点数，精度3位. */
    String NON_NEGATIVE_LESS_EQUAL_1_FLOATING_3_POINT_REGEX = "^0(?:\\.\\d{1,3})?|1(?:\\.0{1,3})?$";

    Pattern NON_NEGATIVE_LESS_EQUAL_1_FLOATING_3_POINT_REGEX_PATTERN =
            Pattern.compile(NON_NEGATIVE_LESS_EQUAL_1_FLOATING_3_POINT_REGEX);

    // ==================================================华丽的分割线==================================================

    /** 字典权限控制校验规则. */
    String DICT_TYPE_ACCESS_VALUE_REGEX = "^([1-4])(,[1-4])*$";

    Pattern DICT_TYPE_ACCESS_VALUE_REGEX_PATTERN = Pattern.compile(DICT_TYPE_ACCESS_VALUE_REGEX);

    /** 字典代码校验规则. */
    String DICT_TYPE_CODE_REGEX = "^(\\w{1,50})$";
    /** {@linkplain #DICT_TYPE_CODE_REGEX}. */
    Pattern DICT_TYPE_CODE_REGEX_PATTERN = Pattern.compile(DICT_TYPE_CODE_REGEX);

    /** 字典项值校验规则. */
    String DICT_ITEM_CODE_REGEX = "^(\\w{1,50})$";
    /** {@linkplain #DICT_ITEM_CODE_REGEX}. */
    Pattern DICT_ITEM_CODE_REGEX_PATTERN = Pattern.compile(DICT_ITEM_CODE_REGEX);

    /** 字典项权限控制校验规则. */
    String DICT_ITEM_ACCESS_VALUE_REGEX = "^([2-4])(,[2-4])*$";
    /** {@linkplain #DICT_ITEM_ACCESS_VALUE_REGEX}. */
    Pattern DICT_ITEM_ACCESS_VALUE_REGEX_PATTERN = Pattern.compile(DICT_ITEM_ACCESS_VALUE_REGEX);

    /** 针对Long类型的ID，如果是单个ID，可以通过该正则校验合法性. */
    String SINGLE_ID_REGEX = "^(\\d{1,20})$";

    /** {@linkplain #SINGLE_ID_REGEX}. */
    Pattern SINGLE_ID_REGEX_PATTERN = Pattern.compile(SINGLE_ID_REGEX);

    /** 针对Long类型的ID，如果是英文逗号分隔的多个ID，可以通过该正则校验合法性. */
    String MULTI_IDS_REGEX = "^(\\d{1,20})(,\\d{1,20})*$";

    /** {@linkplain #MULTI_IDS_REGEX}. */
    Pattern MULTI_IDS_REGEX_PATTERN = Pattern.compile(MULTI_IDS_REGEX);

    /** 系统参数代码校验规则. */
    String PARAM_CODE_REGEX = "^(\\w{1,50})$";
    /** {@linkplain #PARAM_CODE_REGEX}. */
    Pattern PARAM_CODE_REGEX_PATTERN = Pattern.compile(PARAM_CODE_REGEX);

    /** 系统参数值校验规则. */
    String PARAM_VALUE_REGEX = "^(.{1,2000})$";
    /** {@linkplain #PARAM_VALUE_REGEX}. */
    Pattern PARAM_VALUE_REGEX_PATTERN = Pattern.compile(PARAM_VALUE_REGEX);
}
