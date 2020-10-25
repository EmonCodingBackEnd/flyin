package com.coding.flyin.starter.ftp.pool;

import com.coding.flyin.starter.ftp.exception.FTPException;
import com.coding.flyin.starter.ftp.properties.ServerConfig;
import com.coding.flyin.util.SensitiveUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.TimeZone;

/**
 * 池化的FTPClient工厂.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180622 12:16</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class PooledFTPClientFactory extends BasePooledObjectFactory<FTPClient> {

    /** FTP匿名用户. */
    private static final String ANONYMOUS = "anonymous";

    private ServerConfig serverConfig;

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public PooledFTPClientFactory(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();

        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        ftpClient.configure(ftpClientConfig);

        ftpClient.setControlEncoding(serverConfig.getEncoding());
        ftpClient.setConnectTimeout(serverConfig.getConnectTimeout());
        ftpClient.setDataTimeout(serverConfig.getDataTimeout());

        try {
            ftpClient.connect(serverConfig.getHost(), serverConfig.getPort());
        } catch (IOException e) {
            log.error(
                    String.format(
                            "【FTP】连接异常, %s:%s", serverConfig.getHost(), serverConfig.getPort()),
                    e);
            throw e;
        }
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            String msg =
                    String.format(
                            "FTPServer refused connection,replyCode=%s,%s:%s",
                            reply, serverConfig.getHost(), serverConfig.getPort());
            log.error("【FTP】{}", msg);
            throw new FTPException(msg);
        }
        log.info(
                "【FTP】连接成功,replyCode={},{}:{}",
                reply,
                serverConfig.getHost(),
                serverConfig.getPort());

        boolean success;
        String username;
        if (StringUtils.isEmpty(serverConfig.getUsername())) {
            success = ftpClient.login(ANONYMOUS, ANONYMOUS);
            username = ANONYMOUS;
        } else {
            success = ftpClient.login(serverConfig.getUsername(), serverConfig.getPassword());
            username = serverConfig.getUsername();
        }
        if (!success) {
            ftpClient.disconnect();
            String msg =
                    String.format(
                            "FTPClient login failed,username=%s,%s:%s",
                            username, serverConfig.getHost(), serverConfig.getPassword());
            log.error("【FTP】{}", msg);
            throw new FTPException(msg);
        }
        log.info(
                "【FTP】FTPClient login success,username={},{}:{}",
                SensitiveUtils.desensitization(username),
                serverConfig.getHost(),
                SensitiveUtils.desensitization(serverConfig.getPassword()));

        ftpClient.setBufferSize(serverConfig.getBufferSize());
        ftpClient.setFileType(serverConfig.getTransferFileType());
        if (serverConfig.isPassiveMode()) {
            ftpClient.enterLocalPassiveMode();
        }
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) throws Exception {
        if (ftpPooled == null) {
            return;
        }

        FTPClient ftpClient = ftpPooled.getObject();
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (IOException e) {
            log.error("【FTP】ftp client logout failed", e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("【FTP】close ftp client failed", e);
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> ftpPooled) {
        FTPClient ftpClient = ftpPooled.getObject();
        try {
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            log.error("【FTP】failed to validate client", e);
        }
        return false;
    }
}
