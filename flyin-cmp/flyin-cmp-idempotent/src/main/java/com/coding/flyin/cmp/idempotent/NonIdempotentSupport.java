package com.coding.flyin.cmp.idempotent;

import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;
import com.coding.flyin.cmp.idempotent.annotation.resolver.config.NonIdempotentConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RSet;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.KryoCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NonIdempotentSupport {

    @Autowired
    public void setRedisson(RedissonClient redisson) {
        NonIdempotentSupport.redisson = redisson;
    }

    @Autowired
    public void setIdempotenceConfig(NonIdempotentConfig nonIdempotentConfig) {
        NonIdempotentSupport.nonIdempotentConfig = nonIdempotentConfig;
    }

    private static RedissonClient redisson;

    private static NonIdempotentConfig nonIdempotentConfig;

    private static Codec codec = new KryoCodec();

    /** 根据标志获取requestId，如果已经存在了一份identity的数据则抛异常. */
    public static String generateRequestId(String identity) {
        RSetCache<String> setCache = redisson.getSetCache(nonIdempotentConfig.getRedisKey(), codec);
        if (StringUtils.isNotEmpty(identity)) {
            if (!setCache.add(identity, 1, TimeUnit.SECONDS)) {
                log.error("【幂等校验】获取requestId太过频繁 identity={}", identity);
                throw new AppException(AppStatus.U0507, "获取requestId太过频繁");
            }
        }
        String requestId = UUID.randomUUID().toString();
        if (!setCache.add(requestId, 5, TimeUnit.MINUTES)) {
            log.error("【幂等校验】获取requestId失，服务端保存失败");
            throw new AppException(AppStatus.U0507, "获取requestId失败");
        }
        return requestId;
    }

    public static boolean validateRequestId(String requestId) {
        if (requestId == null) {
            log.warn("【幂等校验】requestId为空，默认校验结果为 false");
            return false;
        } else {
            RSet<String> set = redisson.getSet(nonIdempotentConfig.getRedisKey(), codec);
            return set.remove(requestId);
        }
    }
}
