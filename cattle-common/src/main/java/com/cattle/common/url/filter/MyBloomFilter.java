package com.cattle.common.url.filter;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class MyBloomFilter implements UrlFilterInterface{

    private RedisTemplate redisTemplate;

    public MyBloomFilter(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    //hash方法数量
    private int hashFunctionNum;

    public int getHashFunctionNum() {
        return hashFunctionNum;
    }

    public void setHashFunctionNum(int hashFunctionNum) {
        this.hashFunctionNum = hashFunctionNum;
    }

    public int[] createHash(String str){
        return BitSetBloomFilter.createHashes(str,hashFunctionNum);
    }

    public boolean add(String str){
        boolean flag = exist(str);
        if(flag){
            int[] vals = createHash(str);
            batchSetBit(vals);
        }
        return flag;
    }

    @Override
    public boolean exist(String str) {
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
}
