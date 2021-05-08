package com.cattle.common.url.filter;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisBloomFilter implements UrlFilterInterface{

    private RedisTemplate redisTemplate;

    public RedisBloomFilter(RedisTemplate redisTemplate){
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

    public boolean add(String str,String key){
        int[] vals = createHash(str);
        return batchSetBit(vals,key);
    }

    @Override
    public boolean exist(String str,String key) {
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
         * 结果有false则代表不存在
         */
        for(Object o : resultList){
            if((boolean)o == false){
                flag = true;
                break;
            }
        }

        return flag;
    }

    private boolean batchSetBit(int[] hashCode,String key){
        boolean flag = false;
        List<Object> resultList = redisTemplate.executePipelined(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                for(int i = 0 ; i < hashCode.length ; i++){
                    connection.setBit(key.getBytes(),hashCode[i],true);
                }
                return null;
            }
        });

        for(Object o : resultList){
            if((boolean)o == false){
                flag = true;
                break;
            }
        }

        return flag;
    }
}
