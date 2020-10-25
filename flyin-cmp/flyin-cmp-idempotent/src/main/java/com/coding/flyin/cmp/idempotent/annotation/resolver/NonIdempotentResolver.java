package com.coding.flyin.cmp.idempotent.annotation.resolver;

import com.coding.flyin.cmp.api.AppRequest;
import com.coding.flyin.cmp.api.AppResponse;
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

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class NonIdempotentResolver {

    @Around(
        value =
                "@annotation(com.coding.flyin.cmp.idempotent.annotation.NonIdempotent) || @within(com.coding.flyin.cmp.idempotent.annotation.NonIdempotent)"
    )
    public Object doValidate(ProceedingJoinPoint point) throws Throwable {
        TargetPoint targetPoint = TargetPoint.createTargetPoint(point);
        Object[] args = targetPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                if (AppRequest.class.isAssignableFrom(arg.getClass())) {
                    AppRequest request = (AppRequest) arg;
                    String requestId = request.getRequestId();
                    boolean validateResult = NonIdempotentSupport.validateRequestId(requestId);
                    if (!validateResult) {
                        if (AppResponse.class.isAssignableFrom(targetPoint.getReturnType())) {
                            AppResponse response =
                                    (AppResponse) targetPoint.getReturnType().newInstance();
                            response.setErrorCode(AppStatus.U0506.getErrorCode());
                            response.setErrorMessage("不允许重复提交！");
                            return response;
                        }
                    }
                }
            }
        }
        return point.proceed();
    }
}
