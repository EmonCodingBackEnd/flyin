package com.coding.flyin.starter.ftp.result;

import com.coding.flyin.starter.ftp.properties.ServerConfig;
import com.coding.flyin.starter.ftp.param.UploadParam;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UploadResult {

    private ServerConfig serverConfig;
    private UploadParam uploadParam;

    private Map<String, UploadResultItem> successMap = new HashMap<>();
    private Map<String, UploadResultItem> failureMap = new HashMap<>();

    public boolean hasFailure() {
        return !failureMap.isEmpty();
    }

    public void addResultItem(UploadResultItem item) {
        if (item.isSuccess()) {
            successMap.put(item.getOriginalFilename(), item);
        } else {
            failureMap.put(item.getOriginalFilename(), item);
        }
    }
}
