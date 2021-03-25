package com.cattle.web.util;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.pentaho.di.core.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class MyBloomFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    //hash方法数量
    private int hashFunctionNum;

    public int getHashFunctionNum() {
        return hashFunctionNum;
    }

    public void setHashFunctionNum(int hashFunctionNum) {
        this.hashFunctionNum = hashFunctionNum;
    }

    private int[] createHash(String str){
        return BitSetBloomFilter.createHashes(str,hashFunctionNum);
    }

    public boolean add(String str){
        if(StrUtil.isBlank(str)){
            return false;
        }
        boolean flag = false;
        int[] vals = createHash(str);
        List<Object> resultList = redisTemplate.executePipelined(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                for(int i = 0 ; i < vals.length ; i++){
                    connection.getBit("test".getBytes(),vals[i]);
                }
                return null;
            }
        });

        /**
         * 结果有false则代表不存在，则在对应位置插入数据
         */
        for(Object o : resultList){
            if((boolean)o == false){
                flag = true;
                break;
            }
        }

        if(flag){
            batchSetBit(vals);
        }

        return flag;
    }

    private void batchSetBit(int[] hashCode){
        redisTemplate.executePipelined(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                for(int i = 0 ; i < hashCode.length ; i++){
                    connection.setBit("test".getBytes(),hashCode[i],true);
                }
                return null;
            }
        });
    }

    @Test
    public void testRedisBloomFilter(){
        redisTemplate.delete("test");
        setHashFunctionNum(3);
        String same1 = "你是狗";
        String same2 = "你是真的狗";
        String same3 = "www.baidu.com";
        String same4 = "你是狗";
        String[] arr = new String[]{same1,same2,same3,same4};
        Arrays.stream(arr).forEach(same -> {
            System.out.print(same + "-------" + add(same) + "------");
            int[] index = createHash(same);
            System.out.println(Arrays.toString(index));
        });
    }

    @Test
    public void testRedisBloomFilterErrorRate(){
        log.info("开始测试----------------");
        setHashFunctionNum(3);
        redisTemplate.delete("test");
        int errorNum = 0;
        int size = 1000000;
        for(int i = 0 ; i < size ; i++){
            String uuid = UUIDUtil.getUUIDAsString();
            System.out.print(Arrays.toString(createHash(uuid)));
            if(!add(uuid)){
                errorNum++;
            }
        }
        log.info("测试{}条数据，错误率:{}",size,errorNum == 0 ? 0 : errorNum / size);
        log.info("测试完成！");
    }
}
