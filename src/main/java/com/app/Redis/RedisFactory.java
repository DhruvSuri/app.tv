package com.app.Redis;



/**
 * Created by dhruv.suri on 12/04/17.
 */
public class RedisFactory {
    private static final String host = "localhost";

    public static void image(String imageUrl) {
        getRedisConnection().rpush("IMAGE", imageUrl);
    }

    public static String image() {
        return getRedisConnection().lpop("IMAGE");
    }

    public static void video(String videoUrl) {
        getRedisConnection().rpush("VIDEO", videoUrl);
    }

    public static String video() {
        return getRedisConnection().lpop("VIDEO");
    }

    public static void keyword(String imageUrl, String keywords) {
        getRedisConnection().rpush("KEY", imageUrl, keywords);
    }

    public static String keyword() {
        return getRedisConnection().lpop("KEY");
    }

    public static Jedis getRedisConnection(){
        return new Jedis(host);
    }
}
