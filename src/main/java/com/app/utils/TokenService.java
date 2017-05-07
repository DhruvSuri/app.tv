package com.app.utils;

import com.app.Mongo.MongoFactory;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by home on 04/11/16.
 */
public class TokenService {
    //This token service is to prevent multiple notifications getting sent from the Browser
    private static TokenService tokenService = new TokenService();

    public static TokenService getInstance() {
        return tokenService;
    }

    public String getToken() {
        Token token = MongoFactory.getMongoTemplate().findOne(new Query(), Token.class);
        return token.getToken();
    }

    public boolean validateToken(String token) {
        String existingToken = getToken();
        if (token.equals(existingToken)) {
            resetToken();
            return true;
        }
        return false;
    }

    private void resetToken() {
        MongoFactory.getMongoTemplate().dropCollection(Token.class);
        double random = Math.random();
        Token token = new Token();
        token.setToken(random + "");
        MongoFactory.getMongoTemplate().save(token);
    }

    class Token {
        private String id;
        private String token;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
