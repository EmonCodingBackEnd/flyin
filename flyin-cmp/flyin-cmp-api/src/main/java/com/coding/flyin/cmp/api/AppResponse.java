package com.coding.flyin.cmp.api;

import com.coding.flyin.cmp.exception.AppStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class AppResponse<T> implements Serializable {

    private static final long serialVersionUID = -4627150270589944255L;

    protected String errorCode = AppStatus.SUCCESS.getErrorCode();

    protected String errorMessage = AppStatus.SUCCESS.getErrorMessage();

    protected T data;

    public static <T> AppResponse<T> getDefaultResponse() {
        return new AppResponse<T>() {
            private static final long serialVersionUID = 785148869016223225L;
        };
    }
}
