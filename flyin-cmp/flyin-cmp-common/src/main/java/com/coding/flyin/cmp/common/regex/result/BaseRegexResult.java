package com.coding.flyin.cmp.common.regex.result;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式解析结果类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180424 18:08</font><br>
 * 一般而言，使用RegResult就可以了；如果要更方便的获取正则解析结果，可以自定义子类，继承RegexResult。<br>
 * 重点要实现：<br>
 *
 * <ul>
 *   <li>私有无参构造函数
 *   <li>公有静态instance()方法，创建实例
 *   <li>该类及其子类并不是单例模式，但屏蔽了无参构造器的调用，强制统一调用instance构造
 * </ul>
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
public abstract class BaseRegexResult {

    private boolean matched;
    private Map<String, Object> result;
    private Map<String, Object> extraData;

    protected BaseRegexResult() {
        super();
        this.matched = false;
        this.result = new HashMap<>();
        this.extraData = new HashMap<>();
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public <T> T getResult(Class<T> clazz, String key) {
        Object obj = this.result.get(key);
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        return null;
    }

    public String getResult(String key) {
        return (String) this.result.get(key);
    }

    public void setResult(String key, Object value) {
        this.result.put(key, value);
    }

    public String getRawValue() {
        return (String) this.extraData.get("$X_RAW_VALUE");
    }

    public void setRawValue(String rawValue) {
        this.extraData.put("$X_RAW_VALUE", rawValue);
    }

    public String getRegex() {
        return (String) this.extraData.get("$X_REGEX");
    }

    public void setRegex(String value) {
        this.extraData.put("$X_REGEX", value);
    }

    public Pattern getPattern() {
        return (Pattern) this.extraData.get("$X_REGEX_PATTERN");
    }

    public void setPattern(Pattern pattern) {
        this.extraData.put("X_REGEX_PATTERN", pattern);
    }

    public Matcher getMatcher() {
        return (Matcher) this.extraData.get("$X_REGEX_MATCHER");
    }

    public void setMatcher(Matcher matcher) {
        this.extraData.put("$X_REGEX_MATCHER", matcher);
    }
}
