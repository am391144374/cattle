package com.cattle.web.util;

import cn.hutool.core.util.IdUtil;
import com.cattle.common.filter.RedisBloomFilter;
import com.cattle.common.filter.UrlFilterInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

@Slf4j
@SpringBootTest
public class RedisBloomFilterTest {

    @Autowired
    private RedisTemplate redisTemplate;

    String key = "url:filter:test";

    private UrlFilterInterface urlFilterInterface;

    public void init(){
        urlFilterInterface = new RedisBloomFilter();
        redisTemplate.delete(key);
    }

    @Test
    public void testRedisBloomFilter(){
        init();
        String same1 = "你是狗";
        String same2 = "你是真的狗";
        String same3 = "www.baidu.com";
        String same4 = "你是狗";
        String[] arr = new String[]{same1,same2,same3,same4};
        Arrays.stream(arr).forEach(same -> {
            if(urlFilterInterface.exist(same,key)){
                log.info("{} 存在",same);
            }else{
                urlFilterInterface.add(same,key);
                log.info("{} 不存在",same);
            }
        });
    }

    @Test
    public void testRedisBloomFilterErrorRate(){
        init();
        log.info("开始测试----------------");
        int errorNum = 0;
        int size = 1000000;
        for(int i = 0 ; i < size ; i++){
            String stringRes = IdUtil.fastSimpleUUID();
            log.info(stringRes);
            if(urlFilterInterface.exist(stringRes,key)){
                log.info("{} 存在",stringRes);
                errorNum++;
            }else{
                urlFilterInterface.add(stringRes,key);
                log.info("{} 不存在",stringRes);
            }
        }
        log.info("错误数：{}",errorNum);
        log.info("测试{}条数据，错误率:{}",size,errorNum == 0 ? 0 : errorNum / size);
        log.info("测试完成！");
    }

}
