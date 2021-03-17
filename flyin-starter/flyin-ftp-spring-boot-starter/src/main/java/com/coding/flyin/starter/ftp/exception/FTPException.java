package com.coding.flyin.starter.ftp.exception;

/**
 * [请在此输入功能简述].
 *
 * <p>创建时间: <font style="color:#00FFFF">20180622 15:42</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
public class FTPException extends RuntimeException {

    public FTPException(String message) {
        super(message);
    }

    public FTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPException(Throwable cause) {
        super(cause);
    }
}
