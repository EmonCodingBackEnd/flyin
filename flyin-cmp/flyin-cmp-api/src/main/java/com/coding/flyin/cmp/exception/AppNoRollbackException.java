package com.coding.flyin.cmp.exception;

public class AppNoRollbackException extends AppException {

    private static final long serialVersionUID = -1339285197241829179L;

    public AppNoRollbackException(AppStatus appStatus) {
        super(appStatus);
    }

    public AppNoRollbackException(AppStatus appStatus, String errorMessage) {
        super(appStatus, errorMessage);
    }
}
