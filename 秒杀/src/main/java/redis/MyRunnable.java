package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Created by zhoujin on  2018/9/12
 **/
public class MyRunnable implements Runnable {
    String watchKeys = "watchKeys";
    Jedis jedis = new Jedis("132.232.35.101",6379);
    String userInfo;
    public MyRunnable() {
    }

    public MyRunnable(String userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public void run() {
        try {
            jedis.watch(watchKeys);
            String val = jedis.get(watchKeys);
            int valint = Integer.valueOf(val);
            if(valint <= 100 && valint >=1 ) {
                Transaction transaction = jedis.multi();
                transaction.incrBy("watchKeys",-1);
                List<Object> list = transaction.exec();
                if(list == null || list.size() == 0) {
                    String  failUserInfo = "fail" +userInfo;
                    String failInfo = "用户：" + failUserInfo + "商品竞争失败，抢购失败。";
                    System.out.println(failInfo);
                    jedis.setnx(failUserInfo,failInfo);
                } else {
                    for (Object succ : list ){
                        String succUserInfo = "succ" +succ.toString()+userInfo;
                        String succInfo = "用户：" + succUserInfo + "商品抢购成功，当前抢购人数："+ (1-(valint-100));
                        System.out.println(succInfo);
                        jedis.setnx(succUserInfo,succInfo);
                    }
                }
            } else {
                String failuserifo ="kcfail" +  userInfo;
                String failinfo1="用户：" + failuserifo + "商品被抢购完毕，抢购失败";
                System.out.println(failinfo1);
                jedis.setnx(failuserifo, failinfo1);
                // Thread.sleep(500);
                return;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

    }
}
