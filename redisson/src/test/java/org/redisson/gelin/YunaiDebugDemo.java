package org.redisson.gelin;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class YunaiDebugDemo {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        // 创建 RedissonClient 对象
        RedissonClient client = Redisson.create();
        RLock clientLock = client.getLock("xx");
        clientLock.lock();
        // 创建 RRateLimiter 对象
        RRateLimiter rateLimiter = client.getRateLimiter("myRateLimiter");
        // 初始化：最大流速 = 每 1 分钟产生 2 个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < 5; i++) {
            System.out.println(String.format("%s：获得锁结果(%s)", simpleDateFormat.format(new Date()),
                    rateLimiter.tryAcquire()));
            Thread.sleep(250L);
        }
    }

}