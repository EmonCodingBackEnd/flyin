package com.coding.flyin.starter.gray.rule.filter;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import org.springframework.util.StringUtils;

public class RuleFilterFactory {

    public static RuleFilter create(String ruleStr) {
        if (StringUtils.isEmpty(ruleStr)) {
            return null;
        }
        if (ruleStr.contains(GrayConstants.RULE_AND_SPLIT)
                || ruleStr.contains(GrayConstants.RULE_OR_SPLIT)) {
            return createCompose(ruleStr);
        } else {
            return createCommon(ruleStr);
        }
    }

    private static CommonRuleFilter createCommon(String ruleStr) {
        if (StringUtils.isEmpty(ruleStr)) {
            return null;
        }
        String[] arr = ruleStr.split(GrayConstants.RULE_TYPE_TAG_SPLIT);
        if (arr.length > 2) {
            return null;
        }
        if (arr.length == 1) {
            return new CommonRuleFilter(1, arr[0]);
        } else {
            return new CommonRuleFilter(Integer.parseInt(arr[0]), arr[1]);
        }
    }

    private static ComposeRuleFilter createCompose(String ruleStr) {
        if (ruleStr == null) {
            return null;
        }
        if (ruleStr.contains(GrayConstants.RULE_AND_SPLIT)) {
            ComposeRuleFilter rule = new ComposeRuleFilter();
            rule.setLogic(GrayConstants.RULE_AND_NAME);
            for (String _rule : ruleStr.split(GrayConstants.RULE_AND_SPLIT)) {
                rule.addRule(createCommon(_rule));
            }
            return rule;
        } else if (ruleStr.contains(GrayConstants.RULE_OR_SPLIT)) {
            ComposeRuleFilter rule = new ComposeRuleFilter();
            rule.setLogic(GrayConstants.RULE_OR_NAME);
            for (String _rule : ruleStr.split(GrayConstants.RULE_OR_SPLIT)) {
                rule.addRule(createCommon(_rule));
            }
            return rule;
        } else {
            ComposeRuleFilter rule = new ComposeRuleFilter();
            rule.setLogic(GrayConstants.RULE_AND_NAME);
            rule.addRule(createCommon(ruleStr));
            return rule;
        }
    }
}
