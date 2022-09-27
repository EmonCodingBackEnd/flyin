package com.coding.flyin.starter.identifier.exception;

/**
 * configuration error exception
 */
public class ConfigException extends RuntimeException {

    private static final long serialVersionUID = -7397218993101844366L;

    public ConfigException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public ConfigException(final Exception cause) {
        super(cause);
    }
}
