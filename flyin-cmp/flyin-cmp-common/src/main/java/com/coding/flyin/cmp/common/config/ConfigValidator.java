package com.coding.flyin.cmp.common.config;

import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

public interface ConfigValidator {

    Logger log = LoggerFactory.getLogger(ConfigValidator.class);

    boolean isValid();

    static void validate(@NonNull Object config, boolean throwException) {
        final Object[] invalid = {false, null};
        ReflectionUtils.doWithFields(
                config.getClass(),
                field -> {
                    ReflectionUtils.makeAccessible(field);
                    Object object = ReflectionUtils.getField(field, config);
                    String className = field.getDeclaringClass().getSimpleName();
                    String fieldName = field.getName();
                    invalid[1] = className;
                    if (object == null) {
                        log.error("【{}】属性 {} 配置为空，请检查！", className, fieldName);
                        invalid[0] = true;
                    } else {
                        ConfigValidator validator = (ConfigValidator) object;
                        if (!validator.isValid()) {
                            log.error("【{}】属性 {} 配置无效，请检查！{}", className, fieldName, object);
                            invalid[0] = true;
                        }
                    }
                },
                field -> ConfigValidator.class.isAssignableFrom(field.getType()));
        if ((boolean) invalid[0]) {
            String msg = String.format("【%s】配置校验失败！", invalid[1]);
            throw new AppException(AppStatus.S0001, msg);
        }
    }
}
