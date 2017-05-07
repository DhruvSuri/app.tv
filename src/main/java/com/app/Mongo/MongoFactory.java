package com.app.Mongo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by home on 27/06/16.
 */
public class MongoFactory {
    private static MongoFactory mongoFactory = new MongoFactory();
    private static MongoTemplate mongoTemplate;

    private MongoFactory() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");
    }


    public static MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
