package com.cattle.web.util;

import cn.hutool.core.util.IdUtil;
import com.cattle.common.url.filter.MyBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

@Slf4j
@SpringBootTest
public class MyBloomFilterTest {

    @Autowired
    private RedisTemplate redisTemplate;

    private MyBloomFilter myBloomFilter;

    public void init(){
        myBloomFilter = new MyBloomFilter(redisTemplate);
    }

    @Test
    public void testRedisBloomFilter(){
        init();
        redisTemplate.delete("test");
        myBloomFilter.setHashFunctionNum(3);
        String same1 = "你是狗";
        String same2 = "你是真的狗";
        String same3 = "www.baidu.com";
        String same4 = "你是狗";
        String[] arr = new String[]{same1,same2,same3,same4};
        Arrays.stream(arr).forEach(same -> {
            System.out.print(same + "-------" + myBloomFilter.add(same) + "------");
            int[] index = myBloomFilter.createHash(same);
            System.out.println(Arrays.toString(index));
        });
    }

    @Test
    public void testRedisBloomFilterErrorRate(){
        init();
        log.info("开始测试----------------");
        myBloomFilter.setHashFunctionNum(3);
        redisTemplate.delete("test");
        int errorNum = 0;
        int size = 1000000;
        for(int i = 0 ; i < size ; i++){
            String stringRes = IdUtil.fastSimpleUUID();
            log.info(stringRes);
            if(!myBloomFilter.add(stringRes)){
                errorNum++;
            }
        }
        log.info("错误数：{}",errorNum);
        log.info("测试{}条数据，错误率:{}",size,errorNum == 0 ? 0 : errorNum / size);
        log.info("测试完成！");
    }

}
