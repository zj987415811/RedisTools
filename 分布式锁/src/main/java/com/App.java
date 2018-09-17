package com;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
    //加锁过程
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey,String requestId,int expireTime) {
        String result = jedis.set(lockKey,requestId,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,expireTime);
        if(LOCK_SUCCESS.equals(result)){
            return true;
        }
        return false;
    }
    //解锁过程
    public static boolean releaseDistributeLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get',KEYS[1]) == AGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey),Collections.singletonList(requestId));
        if(RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
