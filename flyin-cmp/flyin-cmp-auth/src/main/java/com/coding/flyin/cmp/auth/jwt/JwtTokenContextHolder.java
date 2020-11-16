package com.coding.flyin.cmp.auth.jwt;

import org.springframework.util.Assert;

public final class JwtTokenContextHolder {

    private static final ThreadLocal<JwtTokenContext> jwtTokenContextHolder =
            new InheritableThreadLocal<>();

    public static void clearJwtTokenContext() {
        jwtTokenContextHolder.remove();
    }

    public static JwtTokenContext getJwtTokenContext() {
        JwtTokenContext ctx = jwtTokenContextHolder.get();

        if (ctx == null) {
            ctx = createEmptyJwtTokenContext();
            jwtTokenContextHolder.set(ctx);
        }

        return ctx;
    }

    public static void setJwtTokenContext(JwtTokenContext context) {
        Assert.notNull(context, "Only non-null JwtTokenContext instances are permitted");
        jwtTokenContextHolder.set(context);
    }

    private static JwtTokenContext createEmptyJwtTokenContext() {
        return new JwtTokenContext();
    }
}
