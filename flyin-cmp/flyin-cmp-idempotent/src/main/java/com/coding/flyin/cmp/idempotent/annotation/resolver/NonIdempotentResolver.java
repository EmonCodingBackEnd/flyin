package com.coding.flyin.cmp.idempotent.annotation.resolver;

import com.coding.flyin.cmp.api.AppRequest;
import com.coding.flyin.cmp.api.AppResponse;
import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;
import com.coding.flyin.cmp.idempotent.NonIdempotentSupport;
import com.coding.flyin.core.annotation.support.TargetPoint;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class NonIdempotentResolver {

    @Around(
            value =
                    "@annotation(com.coding.flyin.cmp.idempotent.annotation.NonIdempotent) || @within(com.coding.flyin.cmp.idempotent.annotation.NonIdempotent)")
    public Object doValidate(ProceedingJoinPoint point) throws Throwable {
        TargetPoint targetPoint = TargetPoint.createTargetPoint(point);
        Object[] args = targetPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                if (AppRequest.class.isAssignableFrom(arg.getClass())) {
                    AppRequest request = (AppRequest) arg;
                    String requestId = request.getRequestId();
                    boolean validateResult;
                    String errorMessage;
                    if (StringUtils.isEmpty(requestId)) {
                        validateResult = false;
                        errorMessage = "非幂等接口调用时需要 requestId 参数！";
                    } else {
                        validateResult = NonIdempotentSupport.validateRequestId(requestId);
                        errorMessage = "不允许重复提交！";
                    }
                    if (!validateResult) {
                        if (AppResponse.class.isAssignableFrom(targetPoint.getReturnType())) {
                            AppResponse response =
                                    (AppResponse) targetPoint.getReturnType().newInstance();
                            response.setErrorCode(AppStatus.U0506.getErrorCode());
                            response.setErrorMessage(errorMessage);
                            return response;
                        } else {
                            throw new AppException(AppStatus.U0506, errorMessage);
                        }
                    }
                }
            }
        }
        return point.proceed();
    }
}
