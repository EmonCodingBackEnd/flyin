# flyin-ftp-spring-boot-starter

`flyin-ftp-spring-boot-starter`用于帮助你在Spring Boot项目中轻松集成带有连接池的FTP客户端工具。

## 如何使用

1. 在SpringBoot项目中加入`flyin-ftp-spring-boot-starter`依赖

`Maven`

```xml
<dependency>
    <groupId>com.coding.flyin</groupId>
    <artifactId>flyin-ftp-spring-boot-starter</artifactId>
    <version>0.1.2</version> <!-- 需要替换为最新版本 -->
</dependency>
```

2. 添加配置

```yaml
flyin:
  ftp:
    enabled: true
    server:
      access-url-prefixes: http://file.emon.vip/
      host: 192.168.1.116
      username: ftp
      password: ftp123
      passive-mode: true
    pool: # 非必须的pool属性
      max-wait-millis: 6000
```

3. 引入使用

```java
@Autowired
private FTPTemplate ftpTemplate;

@Autowired
private FTPTools ftpTools; // 可以查阅FTPTools代码了解调用方式
```

## 如何配置多个FTP服务器连接

1. 添加配置

```yaml
flyin:
  ftp:
    enabled: true
    one:
      server:
        access-url-prefixes: http://file.emon.vip/
        host: 192.168.1.116
        username: ftp
        password: ftp123
        passive-mode: true
    two:
      server:
        access-url-prefixes: http://file.emon.vip/
        host: 192.168.1.117
        username: ftp
        password: ftp123
        passive-mode: true
      pool:
        max-total: 10
```

2. 创建多个连接

```java
@Bean
@ConfigurationProperties(prefix = "flyin.ftp.one")
public PooledFTPProperties ftpPropertiesOne() {
    return new PooledFTPProperties();
}

@Bean
public FTPTemplate ftpTemplateOne() {
    PooledFTPClientFactory factory = new PooledFTPClientFactory(ftpPropertiesOne().getServer());
    GenericFTPClientPool pool = new GenericFTPClientPool(factory, ftpPropertiesOne().getPool());
    return new FTPTemplate(pool);
}

@Bean
public FTPTools ftpToolsOne() {
    return new FTPTools(ftpTemplateOne());
}

@Bean
@ConfigurationProperties(prefix = "flyin.ftp.two")
public PooledFTPProperties ftpPropertiesTwo() {
    return new PooledFTPProperties();
}

@Bean
public FTPTemplate ftpTemplateTwo() {
    PooledFTPClientFactory factory = new PooledFTPClientFactory(ftpPropertiesTwo().getServer());
    GenericFTPClientPool pool = new GenericFTPClientPool(factory, ftpPropertiesTwo().getPool());
    return new FTPTemplate(pool);
}

@Bean
public FTPTools ftpToolsTwo() {
    return new FTPTools(ftpTemplateTwo());
}
```

3. 引入使用

```java
@Autowired
@Qualifier("ftpTemplateOne")
private FTPTemplate ftpTemplateOne;

@Autowired
@Qualifier("ftpTemplateTwo")
private FTPTemplate ftpTemplateTwo;

@Autowired
@Qualifier("ftpToolsOne")
private FTPTools ftpToolsOne;

@Autowired
@Qualifier("ftpToolsTwo")
private FTPTools ftpToolsTwo;
```



