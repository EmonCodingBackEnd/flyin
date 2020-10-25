package com.coding.flyin.starter.ftp;

import com.coding.flyin.starter.ftp.param.*;
import com.coding.flyin.starter.ftp.result.DeleteResult;
import com.coding.flyin.starter.ftp.result.DownloadResult;
import com.coding.flyin.starter.ftp.result.ListResult;
import com.coding.flyin.starter.ftp.result.UploadResult;
import com.coding.flyin.starter.ftp.template.FTPTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * FTP工具类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180621 00:44</font><br>
 * 我支持的比你看到的多的多，扩展起来也不麻烦，需要了直接联系我。<br>
 * 更强大的用法请看 ftpTemplate
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class FTPTools {

    public FTPTools(FTPTemplate ftpTemplate) {
        this.ftpTemplate = ftpTemplate;
    }

    private final FTPTemplate ftpTemplate;

    public UploadResult uploadFileAutoDetectDirectory(String filename, String fileContent) {
        UploadParam uploadParam =
                UploadParamBuilder.custom().autoDetect(true).content(filename, fileContent).build();
        return ftpTemplate.uploadFile(uploadParam);
    }

    public UploadResult uploadFileAutoDetectDirectory(
            List<MultipartFile> multipartFileList) {
        UploadParam uploadParam =
                UploadParamBuilder.custom()
                        .autoDetect(true)
                        .multipartFileList(multipartFileList)
                        .build();
        return ftpTemplate.uploadFile(uploadParam);
    }

    public UploadResult uploadFileAutoDetectDirectory(MultipartFile multipartFile) {
        UploadParam uploadParam =
                UploadParamBuilder.custom().autoDetect(true).multipartFile(multipartFile).build();
        return ftpTemplate.uploadFile(uploadParam);
    }

    public UploadResult uploadFileAutoDetectDirectory(File file) {
        UploadParam uploadParam = UploadParamBuilder.custom().autoDetect(true).file(file).build();
        return ftpTemplate.uploadFile(uploadParam);
    }

    public UploadResult uploadFile(
            String remoteDirectory, String filename, String fileContent) {
        UploadParam uploadParam =
                UploadParamBuilder.custom()
                        .remoteDirectory(remoteDirectory)
                        .content(filename, fileContent)
                        .build();
        return ftpTemplate.uploadFile(uploadParam);
    }

    public UploadResult uploadFile(String remoteDirectory, MultipartFile multipartFile) {
        UploadParam uploadParam =
                UploadParamBuilder.custom()
                        .remoteDirectory(remoteDirectory)
                        .multipartFile(multipartFile)
                        .build();
        return ftpTemplate.uploadFile(uploadParam);
    }

    public ListResult listFiles(String remoteDirectory, FTPFileFilter ftpFileFilter) {
        ListParam listParam =
                ListParamBuilder.custom()
                        .remoteDirectory(remoteDirectory)
                        .filter(ftpFileFilter)
                        .build();
        return ftpTemplate.listFiles(listParam);
    }

    public DownloadResult downloadFile(String remoteDirectory, FTPFileFilter ftpFileFilter) {
        DownloadParam downloadParam =
                DownloadParamBuilder.custom()
                        .remoteDirectory(remoteDirectory)
                        .filter(ftpFileFilter)
                        .build();
        return ftpTemplate.downloadFile(downloadParam);
    }

    public DeleteResult deleteFile(String remoteDirectory, FTPFileFilter ftpFileFilter) {
        DeleteParam deleteParam =
                DeleteParamBuilder.custom()
                        .remoteDirectory(remoteDirectory)
                        .filter(ftpFileFilter)
                        .build();
        return ftpTemplate.deleteFile(deleteParam);
    }
}
