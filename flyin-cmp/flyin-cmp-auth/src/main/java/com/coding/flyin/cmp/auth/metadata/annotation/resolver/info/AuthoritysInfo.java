package com.coding.flyin.cmp.auth.metadata.annotation.resolver.info;

import com.coding.flyin.cmp.auth.metadata.annotation.Authority;
import com.coding.flyin.cmp.auth.metadata.annotation.Authoritys;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限校验注解的数据包装类，方便缓存.
 *
 * <p>创建时间: <font style="color:#00FFFF">20210510 22:31</font><br>
 * [请在此输入功能详述]
 *
 * @author emon
 * @version 0.1.30
 * @since 0.1.30
 */
@Setter
@Slf4j
public class AuthoritysInfo {
    private static ExpressionParser parser = new SpelExpressionParser();

    private List<AuthorityInfo> authorityDataList = new ArrayList<>();

    public AuthoritysInfo() {}

    /**
     * 构造权限校验信息.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210510 22:52</font><br>
     * 根据入参 {@linkplain Authoritys 一组权限校验注解} 和 {@linkplain Authority 单个权限校验注解} 构造权限校验信息，优先使用前者
     *
     * @param authoritys - 一组权限校验注解
     * @param authority - 单个权限校验注解
     * @author emon
     * @since 0.1.30
     */
    public AuthoritysInfo(Authoritys authoritys, Authority authority) {
        if (authoritys != null) {
            for (Authority authy : authoritys.value()) {
                authorityDataList.add(new AuthorityInfo(authy));
            }
        } else if (authority != null) {
            authorityDataList.add(new AuthorityInfo(authority));
        }
    }

    /**
     * 获取接口访问需要的权限，返回null表示不需要任何权限.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210510 22:27</font><br>
     *
     * @param contextMap - {@linkplain Authority 单个权限校验注解定义的 spel 表达式求值时依赖的上下文}
     * @return AuthorityInfo
     * @author emon
     * @since 0.1.30
     */
    public AuthorityInfo getAuthorityInfo(@NonNull Map<String, Object> contextMap) {
        AuthorityInfo matchedAuthorityInfo = null;
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(contextMap);
        for (AuthorityInfo authorityInfo : authorityDataList) {
            String spel = authorityInfo.getSpel();
            if (StringUtils.isEmpty(spel)) {
                matchedAuthorityInfo = authorityInfo;
                break;
            }
            Object parsedValue = null;
            try {
                parsedValue = parser.parseExpression(spel).getValue(context);
            } catch (ParseException | EvaluationException e) {
                log.warn("spel={}, contextMap={}, exp={}", spel, contextMap, e.getMessage());
            }
            if (parsedValue instanceof Boolean && Boolean.parseBoolean(parsedValue.toString())) {
                matchedAuthorityInfo = authorityInfo;
                break;
            }
        }
        return matchedAuthorityInfo;
    }

    @Getter
    @Setter
    public static class AuthorityInfo implements Serializable {

        private static final long serialVersionUID = 172683027601727363L;

        public AuthorityInfo() {}

        public AuthorityInfo(Authority authority) {
            this.message = authority.message();
            this.spel = authority.spel();
            this.value = Arrays.stream(authority.value()).collect(Collectors.toSet());
        }
        /** 权限不足时描述信息. */
        private String message;

        /** spel表达式. */
        private String spel;

        /** 需要拥有的权限 */
        private Set<String> value;
    }
}
