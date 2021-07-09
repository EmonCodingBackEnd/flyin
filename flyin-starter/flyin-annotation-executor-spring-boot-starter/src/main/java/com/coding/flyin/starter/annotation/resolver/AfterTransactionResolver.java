package com.coding.flyin.starter.annotation.resolver;

import com.coding.flyin.starter.annotation.AfterTransaction;
import com.coding.flyin.starter.annotation.resolver.support.AfterTransactionContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

///** 越小的order执行优先级越高 */
//@Order(Integer.MIN_VALUE + 10000)
@Aspect
@Component
@Slf4j
public class AfterTransactionResolver {

    @Pointcut("@annotation(afterTransaction)")
    public void publicMethods(AfterTransaction afterTransaction) {}

    @Around(value = "publicMethods(afterTransaction)", argNames = "jp,afterTransaction")
    public Object instrumentMetered(ProceedingJoinPoint jp, AfterTransaction afterTransaction)
            throws Throwable {
        // 被拦截方法的执行结果
        Object result;
        result = jp.proceed();

        // 方法事务执行后回调
        if (AfterTransactionContextHolder.getAfterTransactionWatch() != null) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            // 如果方法执行成功
                            try {
                                AfterTransactionContextHolder.getAfterTransactionWatch().watch();
                            } finally {
                                AfterTransactionContextHolder.clearAfterTransactionWatch();
                            }
                        }
                    });
        }
        return result;
    }
}
