package com.coding.flyin.starter.annotation.resolver.support;

import org.springframework.util.Assert;

public final class AfterTransactionContextHolder {

    private static final ThreadLocal<AfterTransactionWatch> afterTransactionContextWatchHolder =
            new InheritableThreadLocal<>();

    public static void clearAfterTransactionWatch() {
        afterTransactionContextWatchHolder.remove();
    }

    public static AfterTransactionWatch getAfterTransactionWatch() {
        return afterTransactionContextWatchHolder.get();
    }

    public static void setAfterTransactionWatch(AfterTransactionWatch context) {
        Assert.notNull(context, "Only non-null AfterTransactionWatch instances are permitted");
        afterTransactionContextWatchHolder.set(context);
    }
}
