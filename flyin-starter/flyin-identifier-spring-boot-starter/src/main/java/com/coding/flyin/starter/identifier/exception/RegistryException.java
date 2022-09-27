package com.coding.flyin.starter.identifier.exception;

/**
 * 注册中心异常.
 */
public final class RegistryException extends RuntimeException {

    private static final long serialVersionUID = -6417179023552012152L;

    public RegistryException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public RegistryException(final Exception cause) {
        super(cause);
    }
}
