package redis;

import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        final String watchKeys = "watchKeys";
        ExecutorService executor = Executors.newFixedThreadPool(20);
        final Jedis jedis = new Jedis("132.232.35.101",6379);
        jedis.set(watchKeys,"100");
        jedis.close();
        for (int i = 0;i<1000;i++) {
            executor.execute(new MyRunnable("user"+ getRandomString(6)));
        }
        executor.shutdown();
    }

    private static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<length;i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
