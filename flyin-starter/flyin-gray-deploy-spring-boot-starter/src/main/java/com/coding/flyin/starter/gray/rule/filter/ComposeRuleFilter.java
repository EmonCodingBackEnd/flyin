package com.coding.flyin.starter.gray.rule.filter;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.netflix.loadbalancer.Server;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Data
public class ComposeRuleFilter implements RuleFilter {

    private List<RuleFilter> rules;

    private String logic;

    @Override
    public Collection<Server> filter(Collection<Server> serverList) {
        if (CollectionUtils.isEmpty(rules)) {
            return serverList;
        }
        Set<Server> result = new HashSet<>();
        if (GrayConstants.RULE_AND_NAME.equals(logic)) {
            result.addAll(serverList);
            for (RuleFilter rule : rules) {
                result.retainAll(rule.filter(result));
            }
        } else if (GrayConstants.RULE_OR_NAME.equals(logic)) {
            for (RuleFilter rule : rules) {
                result.addAll(rule.filter(serverList));
            }
        }
        return result;
    }

    @Override
    public String toRule() {
        return StringUtils.collectionToDelimitedString(
                rules,
                GrayConstants.RULE_AND_NAME.equals(logic)
                        ? GrayConstants.RULE_AND_SPLIT
                        : GrayConstants.RULE_OR_SPLIT);
    }

    @Override
    public String toString() {
        return toRule();
    }

    public void addRule(RuleFilter rule) {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        rules.add(rule);
    }

    public void removeRule(RuleFilter rule) {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        rules.remove(rule);
    }
}
