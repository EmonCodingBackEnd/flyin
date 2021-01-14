# flyin-http-spring-boot-starter

`flyin-http-spring-boot-starter`用于帮助你在Spring Boot项目中轻松集成Apac的http客户端工具。

## 如何使用

1. 在SpringBoot项目中加入`flyin-http-spring-boot-starter依赖

`Maven`

```xml
<dependency>
    <groupId>com.coding.flyin</groupId>
    <artifactId>flyin-http-spring-boot-starter</artifactId>
    <version>0.1.5</version> <!-- 需要替换为最新版本 -->
</dependency>
```

2. 添加配置

```yaml
flyin:
  http: # 所有属性都不是必须配置的
    max-total: 50
    default-max-per-route: 5
    validate-after-inactivity: 2000
    connect-timeout: 3000
    socket-timeout: 5000
    connection-request-timeout: 3000
    max-per-route-map:
      "[http://file.emon.vip]": 2
      "[http://www.baidu.com]": 10
    keep-alive-host-map:
      "[http://file.emon.vip]": 10
      "[http://www.baidu.com]": 5
```

3. 加入以上配置后，会有一个默认配置的基础版`HttpSyncTools`实例，如果不想使用这个实例，可以自定义：

添加一个微信加密访问的实例：

```java
package com.coding.site.admin.common.http;

import com.coding.flyin.starter.http.HttpSyncTools;
import com.coding.flyin.starter.http.generator.HttpSyncClientGenerator;
import com.coding.flyin.starter.http.properties.HttpProperties;
import com.coding.site.common.ConstDefine;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class HttpSyncToolsConfig {

    @Autowired private HttpProperties httpProperties;

    @Bean
    public HttpSyncTools httpSyncTools() {
        HttpSyncClientGenerator generator = new HttpSyncClientGenerator(httpProperties);
        CloseableHttpClient httpSyncClient = generator.pool().timeout().build();
        return new HttpSyncTools(httpSyncClient);
    }

    @Bean
    public HttpSyncTools httpSyncWechatTools() {
        HttpSyncClientGenerator generator = new HttpSyncClientGenerator(httpProperties);
        CloseableHttpClient httpSyncClient =
                generator
                        .ssl(
                                "PKCS12",
                                ConstDefine.C_PAY.LKY_CERTLOCALPATH,
                                ConstDefine.C_PAY.LKY_CERTPASSWORD)
                        .pool()
                        .timeout(6000, 10000, 6000)
                        .build();
        return new HttpSyncTools(httpSyncClient);
    }
}
```

4. 使用自定义实例

```java
    @Autowired
    @Qualifier("httpSyncWechatTools")
    private HttpSyncTools httpSyncTools;

    @GetMapping("/fsite/http/sync/apache")
    public AppResponse httpSyncApache() {
        String url = "http://file.emon.vip";
        String result;
        try {
            result = httpSyncTools.doGet(url);
        } catch (IOException e) {
            log.error("【httpSyncApache】执行异常", e);
            throw new AppException(AppStatus.S0001, "执行异常");
        }
        log.info(result);
        return AppResponse.getDefaultResponse();
    }
```



