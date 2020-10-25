package com.coding.flyin.cmp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 8623276252895911775L;

    protected String errorCode;

    protected String errorMessage;

    public AppException(AppStatus appStatus) {
        super(appStatus.getErrorMessage());
        this.errorCode = appStatus.getErrorCode();
        this.errorMessage = appStatus.getErrorMessage();
    }

    public AppException(AppStatus appStatus, String errorMessage) {
        super(errorMessage);
        this.errorCode = appStatus.getErrorCode();
        this.errorMessage = errorMessage;
    }
}
