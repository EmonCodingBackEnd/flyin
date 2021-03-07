package com.coding.flyin.cmp.idempotent;

import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;
import com.coding.flyin.cmp.idempotent.annotation.resolver.config.NonIdempotentConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
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

    private static final Codec codec = new KryoCodec();

    /** 根据标志获取requestId，冷却时间是1秒，1秒之内不允许获取第二次；如果已经存在了一份identity的数据则抛异常. */
    public static String generateRequestId(String identity) {
        return generateRequestId(identity, 1, TimeUnit.SECONDS);
    }

    /**
     * 生成非幂等性接口所需参数 requestId
     *
     * @param identity - 根据标志获取requestId，如果已经存在了一份identity的数据则抛异常.
     * @param coolDownTime - 冷却时间，多长时间允许生成一次
     * @param timeUnit - 时间单位
     * @return
     */
    public static String generateRequestId(String identity, long coolDownTime, TimeUnit timeUnit) {
        RMapCache<String, String> mapCache =
                redisson.getMapCache(nonIdempotentConfig.getRedisKey(), codec);
        if (StringUtils.isNotEmpty(identity)) {
            if (mapCache.containsKey(identity)) {
                log.error("【幂等校验】获取requestId太过频繁 identity={}", identity);
                throw new AppException(AppStatus.U0507, "获取requestId太过频繁");
            }
            mapCache.put(identity, identity, coolDownTime, timeUnit);
        }
        String requestId = UUID.randomUUID().toString().replace("-", "");
        mapCache.put(requestId, requestId, 5, TimeUnit.MINUTES);
        return requestId;
    }

    public static boolean validateRequestId(String requestId) {
        if (requestId == null) {
            log.warn("【幂等校验】requestId为空，默认校验结果为 false");
            return false;
        } else {
            RMapCache<String, String> mapCache =
                    redisson.getMapCache(nonIdempotentConfig.getRedisKey(), codec);
            return mapCache.remove(requestId, requestId);
        }
    }
}
