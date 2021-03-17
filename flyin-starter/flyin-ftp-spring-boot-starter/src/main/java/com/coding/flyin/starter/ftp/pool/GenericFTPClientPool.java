package com.coding.flyin.starter.ftp.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * FTPClient连接池.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180622 14:56</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
public class GenericFTPClientPool extends GenericObjectPool<FTPClient> {

    public GenericFTPClientPool(
            PooledFTPClientFactory factory, GenericObjectPoolConfig<FTPClient> config) {
        super(factory, config);
    }
}
