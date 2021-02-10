package com.coding.flyin.starter.gray.properties;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ConfigurationProperties(prefix = "flyin.gray.request.rule")
public class RequestRuleProperties {

    /** 向下请求规则内容. */
    private String data = GrayConstants.EMPTY;

    /** 传递模式. {@linkplain TransferModeEnum 参见} */
    private TransferModeEnum transferMode = TransferModeEnum.OVERRIDE_FIRST;

    public String updateRule(RuleFilter rule) {
        if (rule == null) {
            return this.data;
        }
        switch (transferMode) {
            case OVERRIDE_FIRST:
                if (StringUtils.isEmpty(this.data)) {
                    return rule.toRule();
                } else {
                    return this.data;
                }
            case PENETRATE:
                return rule.toRule();
            case OVERRIDE:
                return this.data;
            case CONCAT:
                if (StringUtils.isEmpty(this.data)) {
                    return rule.toRule();
                } else {
                    return rule.toRule().concat("&").concat(this.data);
                }
            default:
                return rule.toRule();
        }
    }
}
