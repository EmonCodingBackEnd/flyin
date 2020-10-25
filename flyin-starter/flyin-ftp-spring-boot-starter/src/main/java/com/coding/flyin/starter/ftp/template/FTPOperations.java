package com.coding.flyin.starter.ftp.template;

import com.coding.flyin.starter.ftp.exception.FTPException;
import com.coding.flyin.starter.ftp.param.DeleteParam;
import com.coding.flyin.starter.ftp.param.DownloadParam;
import com.coding.flyin.starter.ftp.param.ListParam;
import com.coding.flyin.starter.ftp.param.UploadParam;
import com.coding.flyin.starter.ftp.result.DeleteResult;
import com.coding.flyin.starter.ftp.result.DownloadResult;
import com.coding.flyin.starter.ftp.result.ListResult;
import com.coding.flyin.starter.ftp.result.UploadResult;

/**
 * FTP操作.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180622 20:22</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
public interface FTPOperations {

    UploadResult uploadFile(UploadParam uploadParam) throws FTPException;

    ListResult listFiles(ListParam listParam) throws FTPException;

    DownloadResult downloadFile(DownloadParam downloadParam) throws FTPException;

    <T> T downloadFile(FTPCallback<T> callback) throws FTPException;

    DeleteResult deleteFile(DeleteParam deleteParam) throws FTPException;
}
