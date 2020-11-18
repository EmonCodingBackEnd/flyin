# flyin-cmp-idempotent

`flyin-cmp-idempotent`用于帮助你在Spring Boot项目中API的幂等性校验。

## 如何使用

1. 在SpringBoot项目中加入`flyin-cmp-idempotent`依赖

```xml
<dependency>
    <groupId>com.coding.flyin</groupId>
    <artifactId>flyin-cmp-idempotent</artifactId>
    <version>0.1.0</version> <!-- 需要替换为最新版本 -->
</dependency>
```

2. 引入配置

```yaml
flyin:
  nonidempotent:
    redis-key: fsite:nonidempotent # 可忽略设置，默认redis-key是 flyin.nonidempotent
```

3. 引入使用

```java
@GetMapping("/fsite/idempotent/generate")
public AppResponse generateIdempotent(String identity) {
    AppResponse<String> appResponse = AppResponse.getDefaultResponse();
    appResponse.setData(NonIdempotentSupport.generateRequestId(identity));
    return appResponse;
}

@NonIdempotent
@GetMapping("/fsite/idempotent/validate")
public AppResponse validateIddempotent(AppRequest appRequest) {
    return AppResponse.getDefaultResponse();
}
```

