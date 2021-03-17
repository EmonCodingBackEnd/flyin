package com.coding.flyin.starter.ftp.template;

import com.coding.flyin.starter.ftp.exception.FTPException;
import org.apache.commons.net.ftp.FTPClient;

/**
 * FTP回调.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180622 22:36</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
public interface FTPCallback<T> {

    /**
     * FTP回调方法.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180623 00:29</font><br>
     * [请在此输入功能详述]
     *
     * @param ftpClient - 无需关注连接与关闭的ftpClient实例
     * @return T - 返回结果
     * @author Rushing0711
     * @since 0.1.0
     */
    T doInCallback(FTPClient ftpClient) throws FTPException;
}
